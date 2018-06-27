import { SET_SCAFFOLD_SET, SET_SCAFFOLD_SHARE_HOLDERS } from '../actions/types';

// State structure example
// state = {
//   [scaffoldAddress]: {
//     address,
//     scaffold,
//     summary,
//     shareHolders,
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
    default:
      return state;
  }
}
