import axios from 'axios';
import {
  CONVERT_CURRENCIES,
  FETCH_SCAFFOLDS,
  FETCH_USER,
  SHOW_MODAL,
  SHOW_WITHDRAWAL_MODAL,
} from './types';

export const fetchUser = () => async dispatch => {
  let res = {};

  try {
    res = await axios.get('/api/users/current');
    dispatch({type: FETCH_USER, payload: res.data});
  } catch (err) {
    console.warn('Error fetching user', err);
    throw err;
  }
};

export const fetchScaffolds = (page = 1, limit = 10) => async dispatch => {
  const offset = (Math.max(page, 1) - 1) * limit;
  const params = {offset, limit};

  try {
    const res = await axios.get('/api/scaffolds', {params});
    dispatch({type: FETCH_SCAFFOLDS, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const generatePublicKey = () => async dispatch => {
  let res = {};
  try {
    res = await axios.get('/api/gen-developer-api-key');
  } catch (err) {
    console.log('Error generating public key', err);
  }

  dispatch({type: FETCH_USER, payload: res.data});
};

const convertCurrencies = conversionValues => async dispatch => {
  // set default values if not sent
  const fromAmount = conversionValues.fromAmount
    ? conversionValues.fromAmount
    : '0';
  const fromCurrency = conversionValues.fromCurrency
    ? conversionValues.fromCurrency
    : 'usd';
  const toCurrency = conversionValues.toCurrency
    ? conversionValues.toCurrency
    : 'eth';

  let res = {};

  const apiRequestUrl = `https://openexchangerates.org/api/convert/${fromAmount}/${fromCurrency}/${toCurrency}?app_id=d34199d67d85445a846040c0cf621510`;

  try {
    res = await axios.get(apiRequestUrl);

    dispatch({type: CONVERT_CURRENCIES, payload: res.data.response});
  } catch (err) {
    console.log('Error in convertCurrencies', err);
  }
};

export const deployContract = (formValues, history) => async dispatch => {
  let res = {};
  dispatch({type: SHOW_MODAL, payload: {showModal: true}});

  try {
    res = await axios.post('/api/scaffolds', formValues);

    history.push('/scaffolds');
    dispatch({
      type: SHOW_MODAL,
      payload: {contract: res.data, showLoader: false},
    });
  } catch (err) {
    const response = err ? err.response : null;
    const status = response ? response.status : ''
    const data = response ? response.data : null;
    const backendMessage = data ? data.message : null;
    const message = status + ': ' + (backendMessage || 'Error in deploy contract');

    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false, error: message},
    });
    console.warn('Error in deploy contract: ' + message);
  }
};

export const closeModal = () => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: false, error: ''}});
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: false}});
};

export {convertCurrencies};
