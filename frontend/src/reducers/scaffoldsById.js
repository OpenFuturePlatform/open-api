import { SET_SCAFFOLD_SET, SET_SCAFFOLD_SHARE_HOLDERS, SET_SCAFFOLD_TRANSACTIONS } from '../actions/types';

// State structure example
// state = {
//   [scaffoldAddress]: {
//     address,
//     scaffold,
//     summary,
//     shareHolders,
//     transactions,
//     error,
//     loading
//   }
// }

export default function(state = {}, action) {
  switch (action.type) {
    case SET_SCAFFOLD_SET: {
      const address = action.payload.address;
      const scaffoldSet = action.payload;
      const oldScaffoldSet = state[address] || {};
      return { ...state, [address]: { ...oldScaffoldSet, ...scaffoldSet } };
    }
    case SET_SCAFFOLD_SHARE_HOLDERS: {
      const address = action.payload.address;
      const shareHolders = action.payload.shareHolders;
      const oldScaffoldSet = state[address] || {};
      return { ...state, [address]: { ...oldScaffoldSet, shareHolders } };
    }
    case SET_SCAFFOLD_TRANSACTIONS: {
      const address = action.payload.address;
      const transactions = action.payload.transactions;
      const oldScaffoldSet = state[address] || {};
      return { ...state, [address]: { ...oldScaffoldSet, transactions } };
    }
    default:
      return state;
  }
}
