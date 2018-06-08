import {SET_CURRENT_ETH_ACCOUNT} from '../actions/types';

const initialState = {
  account: '',
  balance: null,
  trueNetwork: false
};

export default function (state = initialState, action) {
  switch (action.type) {
    case SET_CURRENT_ETH_ACCOUNT:
      return {...state, ...action.payload};
    default:
      return state;
  }
}
