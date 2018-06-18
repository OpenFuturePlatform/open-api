import {FETCH_ONCHAIN_SCAFFOLD_SUMMARY} from '../actions/types';

export default function (state = null, action) {
  switch (action.type) {
    case FETCH_ONCHAIN_SCAFFOLD_SUMMARY:
      return action.payload;
    default:
      return state;
  }
}
