import { getScaffoldTransaction } from '../utils/apiPathes';
import { SET_SCAFFOLD_TRANSACTIONS } from './types';
import { TRANSACTIONS_LIMIT as LIMIT } from '../const/index';
import { apiGet } from './apiRequest';
import { getWeb3Contract } from '../utils/web3';
import { scaffoldEventTypes } from '../const/scaffold-event-types';

export const fetchScaffoldTransactionsFromApi = ({ address }, offset = 0, limit = LIMIT) => async dispatch => {
  const params = { offset, limit };
  const transactions = await dispatch(apiGet(getScaffoldTransaction(address), params));
  dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};

export const fetchScaffoldTransactionsFromChain = scaffold => async dispatch => {
  // const address = scaffold.address;
  const contract = getWeb3Contract(scaffold);
  console.time('hi');
  const temp = await contract.getPastEvents('allEvents', { filter: {}, fromBlock: 0, toBlock: 'latest' });
  console.log(temp.map(event => scaffoldEventTypes[event.returnValues[0]]));
  console.timeEnd('hi');
  // dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};

export const subscribeTransactionsByApi = ({ address }) => async dispatch => {};

export const unsubscribeTransactionsByApi = () => async dispatch => {};
