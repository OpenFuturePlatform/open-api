import {SET_DEV_SHARES} from '../actions/types';

const devShares = (state = [], action) => {
  switch (action.type) {
    case SET_DEV_SHARES:
      return action.payload;
    default:
      return state;
  }
};

export default devShares;
