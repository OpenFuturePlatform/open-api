import {UPDATE_CURRENT_ETHEREUM_SCAFFOLD_INSTANCE} from '../actions/types';

export default function (state = {}, action) {
  switch (action.type) {
    case UPDATE_CURRENT_ETHEREUM_SCAFFOLD_INSTANCE:
      return action.payload;
    default:
      return state;
  }
}
