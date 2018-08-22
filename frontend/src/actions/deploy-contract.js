import web3 from '../utils/web3';
import Eth from 'ethjs';
import ethUtil from 'ethjs-util';
import { CONVERT_CURRENCIES, SHOW_MODAL } from './types';
import { getScaffoldsPath, getScaffoldDoCompile, getScaffoldDoDeploy } from '../utils/apiPathes';
import { apiPost, apiPatch, apiGet } from './apiRequest';
import { contractVersion } from '../adapters/contract-version';
import { getWalletMethod } from '../selectors/getWalletMethod';
import { parseApiError } from '../utils/parseApiError';
import { toChecksumAddress } from '../utils/toChecksumAddress';

const setWebHook = (address, webHook) => async dispatch =>
  await dispatch(apiPatch(getScaffoldsPath(address), { webHook }));

export const deployContractByApi = formValues => async dispatch => {
  const scaffold = await dispatch(apiPost(getScaffoldDoDeploy(), formValues));
  return scaffold.address;
};

export const compileContract = (openKey, properties) => async dispatch => {
  return await dispatch(apiPost(getScaffoldDoCompile(), { openKey, properties }));
};

export const processDeploy = async (contract, bin, platformAddress, formValues) => {
  return await contract
    .deploy({
      data: bin,
      arguments: [
        formValues.developerAddress,
        platformAddress,
        ethUtil.fromAscii(formValues.fiatAmount),
        ethUtil.fromAscii(formValues.currency),
        Eth.toWei(formValues.conversionAmount.toString(), 'ether')
      ]
    })
    .send({ from: formValues.developerAddress });
};

export const deployContractByMetaMask = formValues => async (dispatch, getState) => {
  const {
    globalProperties: { platformAddress }
  } = getState();
  const { abi, bin } = await dispatch(compileContract(formValues.openKey, formValues.properties));
  const contract = new web3.eth.Contract(JSON.parse(abi));
  const newContractInstance = await processDeploy(contract, bin, platformAddress, formValues);
  const address = newContractInstance.options.address;
  await dispatch(apiPost(getScaffoldsPath(), { ...formValues, abi, address }));
  return address;
};

export const deployContract = formValuesInit => async (dispatch, getState) => {
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
      contractAddress = await dispatch(deployContractByApi(serializedFormValues));
    } else {
      contractAddress = await dispatch(deployContractByMetaMask(serializedFormValues));
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
  const fromCurrency = conversionValues.fromCurrency ? conversionValues.fromCurrency : 'usd';
  const toCurrency = conversionValues.toCurrency ? conversionValues.toCurrency : 'eth';
  const apiRequestUrl = `https://openexchangerates.org/api/convert/${fromAmount}/${fromCurrency}/${toCurrency}?app_id=d34199d67d85445a846040c0cf621510`;

  try {
    const data = await dispatch(apiGet(apiRequestUrl));
    dispatch({ type: CONVERT_CURRENCIES, payload: data.response });
    return data.response;
  } catch (err) {
    console.log('Error in convertCurrencies', err);
  }
};
