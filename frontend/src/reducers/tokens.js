import {
  FETCH_USER_TOKENS,
  SET_USER_TOKEN
} from "../actions/types";



export default function (state = {totalCount: 0, list: []}, action) {
  switch (action.type) {
    case FETCH_USER_TOKENS:
      return action.payload;
    default:
      return state;
  }
}
