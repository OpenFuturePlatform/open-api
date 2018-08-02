import { FETCH_USER, SET_WALLET_METHOD, SET_AUTH } from '../actions/types';

export default function(state = { isLoading: true }, action) {
  switch (action.type) {
    case FETCH_USER:
      const payload = action.payload;
      const isLoading = action.isLoading;
      const isApiAllowed = payload.user.roles.map(it => it.key).includes('ROLE_MASTER');
      return { ...payload, isApiAllowed, isLoading };
    case SET_WALLET_METHOD:
      return { ...state, byApiMethod: action.payload };
    case SET_AUTH:
      const isAuthorized = action.payload;
      return { ...state, isAuthorized };
    default:
      return state;
  }
}
