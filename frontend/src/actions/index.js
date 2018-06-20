import axios from 'axios';
import {
  FETCH_USER,
  SHOW_MODAL,
  SHOW_WITHDRAWAL_MODAL,
} from './types';

export const fetchUser = () => async dispatch => {
  try {
    const res = await axios.get('/api/users/current');
    dispatch({type: FETCH_USER, payload: res.data});
  } catch (err) {
    console.warn('Error fetching user', err);
    throw err;
  }
};

export const closeModal = () => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: false, error: '', contract: '', showLoader: true}});
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: false, contract: '', showLoader: true}});
};
