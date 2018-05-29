import {CONVERT_CURRENCIES} from '../actions/types';

export default function (state = null, action) {
  switch (action.type) {
    case CONVERT_CURRENCIES:
      return action.payload || null;
    default:
      return state;
  }
}
