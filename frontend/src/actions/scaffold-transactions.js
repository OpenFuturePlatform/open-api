import { getScaffoldTransaction, updateScaffoldTransactions } from '../utils/apiPathes';
import { SET_SCAFFOLD_TRANSACTIONS, UPDATE_SCAFFOLD_TRANSACTIONS, ADD_MORE_SCAFFOLD_TRANSACTIONS } from './types';
import { TRANSACTIONS_LIMIT as LIMIT } from '../const/index';
import { apiGet } from './apiRequest';
import { getWeb3Contract } from '../utils/web3';
import { scaffoldEventTypes } from '../const/scaffold-event-types';
import { getTransactionSelector } from '../selectors/getTransactionSelector';

const fetchTransactionsFromApiRequest = ({ address }, offset, limit = LIMIT) => async dispatch => {
  const params = { offset, limit };
  return await dispatch(apiGet(getScaffoldTransaction(address), params));
};

export const fetchScaffoldTransactionsFromApi = ({ address }, offset = 0) => async (dispatch, getState) => {
  const limit = getTransactionSelector(getState(), address).limit || LIMIT;
  const transactions = await dispatch(fetchTransactionsFromApiRequest({ address }, offset, limit));
  dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions: transactions } });
};

export const addMoreScaffoldTransactionsFromApi = ({ address }, offset) => async dispatch => {
  const transactions = await dispatch(fetchTransactionsFromApiRequest({ address }, offset));
  dispatch({ type: ADD_MORE_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};

export const fetchScaffoldTransactionsFromChain = scaffold => async () => {
  // const address = scaffold.address;
  const contract = getWeb3Contract(scaffold);
  console.time('>> transactions blockchain fetch');
  const temp = await contract.getPastEvents('allEvents', { filter: {}, fromBlock: 0, toBlock: 'latest' });
  console.log(temp.map(event => scaffoldEventTypes[event.returnValues[0]]));
  console.timeEnd('>> transactions blockchain fetch');
  // dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};

let isSubscribed = false;

const updateTransactions = address => async dispatch => {
  console.timeEnd('>> start update');
  const transactions = await dispatch(apiGet(updateScaffoldTransactions(address)));
  console.log('>> updates: ', transactions);
  if (isSubscribed) {
    console.time('>> start update');
    dispatch(updateTransactions(address));
  }
  if (transactions.totalCount) {
    dispatch({ type: UPDATE_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
  }
};

export const subscribeTransactionsByApi = address => async dispatch => {
  if (isSubscribed) {
    return;
  }
  isSubscribed = true;
  console.time('>> start update');
  dispatch(updateTransactions(address));
};

export const unsubscribeTransactionsByApi = () => async () => (isSubscribed = false);
