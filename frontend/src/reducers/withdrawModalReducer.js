import {SHOW_WITHDRAWAL_MODAL} from '../actions/types';

export default function (state = {
                           showModal: false,
                           transactionHash: '',
                           showLoader: true,
                         },
                         action,) {
  switch (action.type) {
    case SHOW_WITHDRAWAL_MODAL:
      return Object.assign({}, state, action.payload);
    default:
      return state;
  }
}
