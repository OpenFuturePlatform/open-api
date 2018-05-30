import {GET_SCAFFOLD_FIELDS} from '../actions/types';

export default function (state = [], action) {
  const {payload, type} = action;

  switch (type) {
    case GET_SCAFFOLD_FIELDS:
      return payload;
    default:
      return state;
  }
}
