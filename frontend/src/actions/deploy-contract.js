import web3 from '../utils/web3';
import Eth from 'ethjs';
import ethUtil from 'ethjs-util';
import { CONVERT_CURRENCIES, SHOW_MODAL } from './types';
import { serializeScaffold } from '../utils/scaffold-adapter';
import { getScaffoldsPath, getScaffoldDoCompile, getScaffoldDoDeploy } from '../utils/apiPathes';
import { apiPost, apiPatch, apiGet } from './apiRequest';

const setWebHook = (address, webHook) => async dispatch =>
  await dispatch(apiPatch(getScaffoldsPath(address), { webHook }));

export const deployContractByApi = (formValues, history) => async dispatch => {
  dispatch({ type: SHOW_MODAL, payload: { showModal: true } });

  try {
    const scaffold = await dispatch(apiPost(getScaffoldDoDeploy(), serializeScaffold(formValues)));
    if (formValues.webHook) {
      await dispatch(setWebHook(scaffold.address, formValues.webHook));
    }
    dispatch({
      type: SHOW_MODAL,
      payload: { contract: scaffold, showLoader: false }
    });
    return scaffold.address;
  } catch (err) {
    const response = err ? err.response : null;
    const status = response ? response.status : '';
    const data = response ? response.data : null;
    const backendMessage = data ? data.message : null;
    const message = status + ': ' + (backendMessage || 'Error in deploy contract');

    dispatch({
      type: SHOW_MODAL,
      payload: { showLoader: false, error: message }
    });
    throw new Error('Error in deploy contract: ' + message);
  }
};

export const compileContract = (openKey, properties) => async dispatch => {
  return await dispatch(apiPost(getScaffoldDoCompile(), { openKey, properties }));
};

export const processDeploy = async (contract, bin, formValues) => {
  return await contract
    .deploy({
      data: bin,
      arguments: [
        formValues.developerAddress,
        formValues.developerAddress,
        ethUtil.fromAscii(formValues.fiatAmount),
        ethUtil.fromAscii(formValues.currency),
        Eth.toWei(formValues.conversionAmount.toString(), 'ether')
      ]
    })
    .send({ from: formValues.developerAddress })
    .on('error', error => console.log('>> ', error));
};

export const deployContract = formValues => async dispatch => {
  dispatch({ type: SHOW_MODAL, payload: { showModal: true, showLoader: true } });
  try {
    const { abi, bin } = await dispatch(compileContract(formValues.openKey, formValues.properties));
    const contract = new web3.eth.Contract(JSON.parse(abi));
    const newContractInstance = await processDeploy(contract, bin, formValues);

    const address = newContractInstance.options.address;
    const data = await dispatch(apiPost(getScaffoldsPath(), { ...serializeScaffold(formValues), abi, address }));

    if (formValues.webHook) {
      await setWebHook(address, formValues.webHook);
    }

    dispatch({
      type: SHOW_MODAL,
      payload: { contract: data, showLoader: false }
    });

    return address;
  } catch (err) {
    let message = 'Error in deploy contract';
    if (err.response) {
      const response = err ? err.response : null;
      const status = response ? response.status : '';
      const data = response ? response.data : null;
      const backendMessage = data ? data.message : null;
      message = status + ': ' + (backendMessage || 'Error in deploy contract');
    } else {
      message = err.message;
    }

    dispatch({
      type: SHOW_MODAL,
      payload: { showLoader: false, error: message }
    });
    throw new Error(message);
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
