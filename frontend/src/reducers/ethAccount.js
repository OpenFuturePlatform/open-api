import {SET_CURRENT_ETH_ACCOUNT} from '../actions/types';

const initialState = {
  account: '',
  ethAccount: null,
  tokenBalance: null,
  activeNetworkId: null,
  activating: false,
  activatingHash: null
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
