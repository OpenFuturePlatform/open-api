import {SET_DEV_SHARES} from '../actions/types';

const shareHolders = (state = [], action) => {
  switch (action.type) {
    case SET_DEV_SHARES:
      return action.payload;
    default:
      return state;
  }
};

export default shareHolders;
