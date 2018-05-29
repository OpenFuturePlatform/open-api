import axios from 'axios';
// files
import Scaffold from '../utils/scaffold';
import web3 from '../utils/web3';
// actions
import {
  CONVERT_CURRENCIES,
  FETCH_ONCHAIN_SCAFFOLD_SUMMARY,
  FETCH_SCAFFOLDS,
  FETCH_USER,
  SHOW_MODAL,
  SHOW_WITHDRAWAL_MODAL,
  UPDATE_CURRENT_SCAFFOLD_INSTANCE,
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

export const fetchScaffolds = () => async dispatch => {
  let res = {};

  try {
    res = await axios.get('/api/scaffolds');
    dispatch({type: FETCH_SCAFFOLDS, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const fetchSummaryOnchain = params => async dispatch => {
  let res = {};
  const summaryPropertyNames = [
    'scaffoldDescription',
    'balance',
    'fiatAmount',
    'fiatCurrency',
    'scaffoldAmount',
    'transactions',
    'vendorAddress',
  ];

  try {
    res = await axios.get(
      `/api/scaffold/${params.openDevId}/${params.contractAddress}`,
    );

    const scaffold = Scaffold(
      JSON.stringify(res.data),
      params.contractAddress,
    );

    dispatch({
      type: UPDATE_CURRENT_SCAFFOLD_INSTANCE,
      payload: scaffold,
    });

    const scaffoldSummary = await scaffold.methods
      .getScaffoldSummary()
      .call();

    const summaryObject = generateSummaryObject(
      scaffoldSummary,
      summaryPropertyNames,
    );

    dispatch({
      type: FETCH_ONCHAIN_SCAFFOLD_SUMMARY,
      payload: summaryObject,
    });
  } catch (err) {
    console.log('Error getting on-chain summary:', err);
  }
};

export const withdrawFunds = (formValues, scaffold, history) => async dispatch => {
  const accounts = await web3.eth.getAccounts();
  let withdrawalAmountInEther = 0;

  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: true}});

  try {
    // If the requested amount of withdrawal is in a currency other than ether, convert it
    // else the withdrawalAmount is aleady in ether
    if (formValues.withdrawalCurrency !== 'eth') {
      const apiRequestUrl = `https://openexchangerates.org/api/convert/${formValues.withdrawalAmount}/${formValues.withdrawalCurrency}/eth?app_id=d34199d67d85445a846040c0cf621510`;
      const res = await axios.get(apiRequestUrl);
      withdrawalAmountInEther = res.data.response;
    } else {
      withdrawalAmountInEther = formValues.withdrawalAmount;
    }

    // Solidity always wants the amount of any transactions in wei, and web3 wants
    // numbers either as BN, or string.  Convert to wei from Ether, and to string
    const withdrawalAmountInWei = web3.utils.toWei(withdrawalAmountInEther.toString(), 'ether');

    // create an instance of the scaffold and withdraw amount of ether
    // address must be developer address entered when scaffold originally created
    const withdrawalResponse = await scaffold.methods
      .withdrawFunds(withdrawalAmountInWei)
      .send({from: accounts[0]});

    withdrawalResponse.showLoader = false;

    history.push('/scaffolds');
    dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: withdrawalResponse});
  } catch (err) {
    console.log('Error withdrawing funds', err);
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
  } catch (err) {
    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false},
    });
    console.log('Error in deploy contract', err);
  }

  history.push('/scaffolds');
  dispatch({
    type: SHOW_MODAL,
    payload: {contract: res.data, showLoader: false},
  });
};

export const closeModal = () => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: false}});
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: false}});
};

function generateSummaryObject(summary, summaryPropertyNames) {
  let summaryObject = {};
  const summaryKeys = Object.keys(summary);

  for (let key in summaryKeys) {
    summaryObject[summaryPropertyNames[key]] = summary[key];
  }

  return summaryObject;
}

export {convertCurrencies};
