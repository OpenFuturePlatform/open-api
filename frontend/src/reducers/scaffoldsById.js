import {TRANSACTIONS_LIMIT} from '../const/index';
import {
  ADD_MORE_ETHEREUM_SCAFFOLD_TRANSACTIONS,
  SET_ETHEREUM_SCAFFOLD_SET,
  SET_ETHEREUM_SCAFFOLD_SHARE_HOLDERS,
  SET_ETHEREUM_SCAFFOLD_TRANSACTIONS,
  UPDATE_ETHEREUM_SCAFFOLD_TRANSACTIONS
} from '../actions/types';

// State structure example
// state = {
//   [scaffoldAddress]: {
//     address,
//     scaffold,
//     summary,
//     shareHolders,
//     transactions: {list: [], totalCount: 0, limit: 0},
//     error,
//     loading
//   }
// }

const scaffoldTransactionsReducer = (state = { list: [], totalCount: 0, limit: TRANSACTIONS_LIMIT }, action) => {
  switch (action.type) {
    case SET_ETHEREUM_SCAFFOLD_TRANSACTIONS: {
      const { totalCount, list } = action.payload.transactions;
      return { ...state, totalCount, list };
    }
    case ADD_MORE_ETHEREUM_SCAFFOLD_TRANSACTIONS: {
      const { totalCount, list } = action.payload.transactions;
      const newList = [...state.list, ...list];
      const newLimit = state.limit + TRANSACTIONS_LIMIT;
      return { ...state, totalCount, list: newList, limit: newLimit };
    }
    case UPDATE_ETHEREUM_SCAFFOLD_TRANSACTIONS: {
      const { totalCount, list } = action.payload.transactions;
      const newTotalCount = totalCount + state.totalCount;
      const newList = [...list, ...state.list].slice(0, state.limit);
      return { ...state, totalCount: newTotalCount, list: newList };
    }
    default:
      return state;
  }
};

export default function(state = {}, action) {
  switch (action.type) {
    case SET_ETHEREUM_SCAFFOLD_SET: {
      const address = action.payload.address;
      const oldScaffoldSet = state[address] || {};
      const scaffoldSet = action.payload;
      return { ...state, [address]: { ...oldScaffoldSet, ...scaffoldSet } };
    }
    case SET_ETHEREUM_SCAFFOLD_SHARE_HOLDERS: {
      const address = action.payload.address;
      const oldScaffoldSet = state[address] || {};
      const shareHolders = action.payload.shareHolders;
      return { ...state, [address]: { ...oldScaffoldSet, shareHolders } };
    }
    case SET_ETHEREUM_SCAFFOLD_TRANSACTIONS:
    case UPDATE_ETHEREUM_SCAFFOLD_TRANSACTIONS:
    case ADD_MORE_ETHEREUM_SCAFFOLD_TRANSACTIONS: {
      const address = action.payload.address;
      const oldScaffoldSet = state[address] || {};
      const transactions = scaffoldTransactionsReducer(oldScaffoldSet.transactions, action);
      return { ...state, [address]: { ...oldScaffoldSet, transactions } };
    }
    default:
      return state;
  }
}
