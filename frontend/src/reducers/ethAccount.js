import {SET_CURRENT_ETH_ACCOUNT} from '../actions/types';

const initialState = {
  account: '',
  balance: null,
  trueNetwork: false
};

const ethAccount = (state = initialState, action) => {
  switch (action.type) {
    case SET_CURRENT_ETH_ACCOUNT:
      return {...state, ...action.payload};
    default:
      return state;
  }
};

export default ethAccount;
