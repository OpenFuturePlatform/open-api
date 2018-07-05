import axios from 'axios';
import { getScaffoldTransaction } from '../utils/apiPathes';
import { SET_SCAFFOLD_TRANSACTIONS } from './types';
import { TRANSACTIONS_LIMIT } from '../const/index';

export const fetchScaffoldTransactions = (address, offset = 0, limit = TRANSACTIONS_LIMIT) => async dispatch => {
  const params = { offset, limit };
  const { data: transactions } = await axios.get(getScaffoldTransaction(address), { params });
  dispatch({ type: SET_SCAFFOLD_TRANSACTIONS, payload: { address, transactions } });
};
