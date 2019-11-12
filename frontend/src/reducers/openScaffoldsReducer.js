import {FETCH_OPEN_SCAFFOLDS} from '../actions/types';

export default function (state = {totalCount: 0, list: []}, action) {
  switch (action.type) {
    case FETCH_OPEN_SCAFFOLDS:
      return action.payload || [];
    default:
      return state;
  }
}
