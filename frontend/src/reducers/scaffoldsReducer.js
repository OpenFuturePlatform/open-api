import {FETCH_SCAFFOLDS} from '../actions/types';

export default function (state = {totalCount: 0, list: []}, action) {
  switch (action.type) {
    case FETCH_SCAFFOLDS:
      return action.payload;
    default:
      return state;
  }
}
