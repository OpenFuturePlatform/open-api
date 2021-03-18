import web3 from '../utils/web3';
import Eth from 'ethjs-unit';
import {CONVERT_CURRENCIES, SHOW_MODAL} from './types';
import {
  getEthereumScaffoldDoCompile,
  getEthereumScaffoldDoDeploy,
  getEthereumScaffoldsPath,
  getOpenScaffoldsPath
} from '../utils/apiPathes';
import {apiGet, apiPatch, apiPost} from './apiRequest';
import {contractVersion} from '../adapters/contract-version';
import {getWalletMethod} from '../selectors/getWalletMethod';
import {parseApiError} from '../utils/parseApiError';
import {toChecksumAddress} from '../utils/toChecksumAddress';

const setWebHook = (address, webHook) => async dispatch =>
  await dispatch(apiPatch(getEthereumScaffoldsPath(address), { webHook }));

export const deployEthereumContractByApi = formValues => async dispatch => {
  const scaffold = await dispatch(apiPost(getEthereumScaffoldDoDeploy(), formValues));
  return scaffold.address;
};

export const compileEthereumContract = (openKey, properties) => async dispatch => {
  return await dispatch(apiPost(getEthereumScaffoldDoCompile(), { openKey, properties }));
};

export const processDeploy = async (contract, bin, platformAddress, formValues) => {
  return await contract
    .deploy({
      data: bin,
      arguments: [
        formValues.developerAddress,
        platformAddress,
        web3.utils.fromAscii(formValues.fiatAmount),
        web3.utils.fromAscii(formValues.currency),
        Eth.toWei(formValues.conversionAmount, 'ether').toString()
      ]
    })
    .send({ from: formValues.developerAddress });
};

export const deployEthereumContractByMetaMask = formValues => async (dispatch, getState) => {
  const {
    globalProperties: { platformAddress }
  } = getState();
  const { abi, bin } = await dispatch(compileEthereumContract(formValues.openKey, formValues.properties));
  const contract = new web3.eth.Contract(JSON.parse(abi));
  const newContractInstance = await processDeploy(contract, bin, platformAddress, formValues);
  const address = newContractInstance.options.address;
  await dispatch(apiPost(getEthereumScaffoldsPath(), { ...formValues, abi, address }));
  return address;
};

export const deployEthereumContract = formValuesInit => async (dispatch, getState) => {
  dispatch({ type: SHOW_MODAL, payload: { showLoader: true, showModal: true } });
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  try {
    const developerAddress = toChecksumAddress(formValuesInit.developerAddress);
    const vendorAddress = toChecksumAddress(formValuesInit.vendorAddress);
    const formValues = { ...formValuesInit, developerAddress, vendorAddress };
    const version = contractVersion('latest').version();
    const serializedFormValues = contractVersion(version).serializeScaffold({ ...formValues, version });
    let contractAddress;

    if (byApiMethod) {
      contractAddress = await dispatch(deployEthereumContractByApi(serializedFormValues));
    } else {
      contractAddress = await dispatch(deployEthereumContractByMetaMask(serializedFormValues));
    }
    if (formValues.webHook) {
      await setWebHook(contractAddress, formValues.webHook);
    }

    dispatch({
      type: SHOW_MODAL,
      payload: { contract: contractAddress, showLoader: false }
    });

    return contractAddress;
  } catch (e) {
    const error = parseApiError(e);

    dispatch({
      type: SHOW_MODAL,
      payload: { showLoader: false, error: error.message }
    });

    throw error;
  }
};

export const convertCurrencies = conversionValues => async dispatch => {
  const fromAmount = conversionValues.fromAmount ? conversionValues.fromAmount : '0';
  let fromCurrency = conversionValues.fromCurrency ? conversionValues.fromCurrency.toUpperCase() : 'TUSD';
  let toCurrency = conversionValues.toCurrency ? conversionValues.toCurrency.toUpperCase() : 'ETH';
  const apiRequestUrl = `https://api.binance.com/api/v3/avgPrice?symbol=${fromCurrency}${toCurrency}`;

  try {
    const data = await dispatch(apiGet(apiRequestUrl));
    dispatch({ type: CONVERT_CURRENCIES, payload: data.price * fromAmount });
    return data.price * fromAmount;
  } catch (err) {
    console.log('Error in convertCurrencies', err);
  }
};

export const saveOpenScaffold = formValues => async (dispatch) => {
  try {
    const description = formValues.title;
    await dispatch(apiPost(getOpenScaffoldsPath(), {...formValues, description}));
  } catch (e) {
    const error = parseApiError(e);
    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false, showModal: true, error: error.message}
    });
    throw error;
  }
};
