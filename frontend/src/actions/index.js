import axios from 'axios';
import web3 from '../utils/web3';
import {
  CONVERT_CURRENCIES, FETCH_ONCHAIN_SCAFFOLD_SUMMARY,
  FETCH_SCAFFOLDS,
  FETCH_USER, SET_CURRENT_ETH_ACCOUNT,
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

export const fetchScaffoldItem = (scaffoldAddress) => async dispatch => {
  dispatch({type: FETCH_ONCHAIN_SCAFFOLD_SUMMARY, payload: {}});
  try {
    const res = await axios.get(`/api/scaffolds/${scaffoldAddress}/summary`);
    dispatch({type: FETCH_ONCHAIN_SCAFFOLD_SUMMARY, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const deactivateScaffold = (scaffoldAddress) => async dispatch => {
  try {
    await axios.post(`/api/scaffolds/${scaffoldAddress}/doDeactivate`);
    fetchScaffoldItem(scaffoldAddress)(dispatch);
  } catch (err) {
    console.log('Error deactivating scaffolds', err);
  }
};

const convertCurrencies = conversionValues => async dispatch => {
  const fromAmount = conversionValues.fromAmount ? conversionValues.fromAmount : '0';
  const fromCurrency = conversionValues.fromCurrency ? conversionValues.fromCurrency : 'usd';
  const toCurrency = conversionValues.toCurrency ? conversionValues.toCurrency : 'eth';
  let res = {};
  const apiRequestUrl = `https://openexchangerates.org/api/convert/${fromAmount}/${fromCurrency}/${toCurrency}?app_id=d34199d67d85445a846040c0cf621510`;

  try {
    res = await axios.get(apiRequestUrl);
    dispatch({type: CONVERT_CURRENCIES, payload: res.data.response});
    return res.data.response;
  } catch (err) {
    console.log('Error in convertCurrencies', err);
  }
};

export const compileContract = async (openKey, properties) => {
  const response = await axios.post('/api/scaffolds/doCompile', {openKey, properties});
  return response.data;
};

export const processDeploy = async (contract, bin, formValues) => {
  return await contract.deploy({
    data: bin,
    arguments: [
      formValues.developerAddress,
      formValues.description,
      formValues.fiatAmount,
      formValues.currency,
      web3.utils.toWei(formValues.conversionAmount.toString())]
  }).send({
    from: formValues.developerAddress,
    gas: 1450000,
    gasPrice: '10000000000'
  });
};

export const deployContract = (formValues) => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: true}});
  try {
    const {abi, bin} = await compileContract(formValues.openKey, formValues.properties);
    const contract = new web3.eth.Contract(JSON.parse(abi));
    const newContractInstance = await processDeploy(contract, bin, formValues);

    const address = newContractInstance.options.address;
    const response = await axios.post('/api/scaffolds', {...formValues, abi, address});

    dispatch({
      type: SHOW_MODAL,
      payload: {contract: response.data, showLoader: false, showModal: false},
    });

    return response;

  } catch (err) {

    let message = 'Error in deploy contract';
    if (err.message) {
      message = err.message;
    } else {
      const response = err ? err.response : null;
      const status = response ? response.status : '';
      const data = response ? response.data : null;
      const backendMessage = data ? data.message : null;
      message = status + ': ' + (backendMessage || 'Error in deploy contract');
    }

    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false, error: message},
    });
    throw new Error(message);
  }
};

export const closeModal = () => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: false, error: ''}});
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: false}});
};

let ethAccountTimer;

const setEthAccount = (account, dispatch) => {
  if (!account) {
    dispatch({
      type: SET_CURRENT_ETH_ACCOUNT,
      payload: {account: '', balance: null}
    });
    return;
  }
  web3.eth.getBalance(account, (error, balance) => {
    dispatch({
      type: SET_CURRENT_ETH_ACCOUNT,
      payload: {account, balance: Number(balance) / 1000000000000000000}
    })
  });
};

export const subscribeEthAccount = () => async dispatch => {
  if (ethAccountTimer) {
    return;
  }

  let account;
  web3.eth.getAccounts((error, newAccounts) => {
    account = newAccounts[0];
    setEthAccount(account, dispatch);
  });

  ethAccountTimer = setInterval(() => {
    web3.eth.getAccounts((error, newAccounts) => {
      if (newAccounts[0] !== account) {
        account = newAccounts[0];
        setEthAccount(account, dispatch);
      }
    });
  }, 1000);
};

export const unsubscribeEthAccount = () => () => clearInterval(ethAccountTimer);

export {convertCurrencies};
