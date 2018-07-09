import { getScaffoldTransaction } from '../utils/apiPathes';
import { SET_SCAFFOLD_TRANSACTIONS } from './types';
import { TRANSACTIONS_LIMIT } from '../const/index';
import { apiGet } from './apiRequest';

export const fetchScaffoldTransactions = (address, offset = 0, limit = TRANSACTIONS_LIMIT) => async dispatch => {
  const params = { offset, limit };
  const transactions = await dispatch(apiGet(getScaffoldTransaction(address), params));
  dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};
