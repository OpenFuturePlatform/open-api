import { FETCH_USER, SET_WALLET_METHOD } from '../actions/types';

export default function(state = {}, action) {
  switch (action.type) {
    case FETCH_USER:
      const payload = action.payload;
      const isApiAllowed = payload.user.roles.map(it => it.key).includes('ROLE_MASTER');
      return { ...payload, isApiAllowed };
    case SET_WALLET_METHOD:
      return { ...state, byApiMethod: action.payload };
    default:
      return state;
  }
}
