import {SHOW_MODAL} from '../actions/types';

export default function (state = {
                           showModal: false,
                           contract: '',
                           showLoader: true,
                           error: ''
                         },
                         action,) {
  switch (action.type) {
    case SHOW_MODAL:
      return Object.assign({}, state, action.payload);
    default:
      return state;
  }
}
