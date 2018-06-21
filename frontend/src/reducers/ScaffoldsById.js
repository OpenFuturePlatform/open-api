import {SET_SCAFFOLD_ITEM} from '../actions/types';

export default function (state = {}, action) {
  switch (action.type) {
    case SET_SCAFFOLD_ITEM:
      const address = action.payload.address;
      const scaffoldSet = action.payload;
      const oldScaffoldSet = state[address] || {};
      return {...state, [address]: {...oldScaffoldSet, ...scaffoldSet}};
    default:
      return state;
  }
}
