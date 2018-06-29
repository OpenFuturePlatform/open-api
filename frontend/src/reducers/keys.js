import { FETCH_KEYS } from '../actions/types';

export default function(state = [], action) {
  switch (action.type) {
    case FETCH_KEYS:
      return action.payload;
    default:
      return state;
  }
}
