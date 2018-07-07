import axios from 'axios';
import { FETCH_USER, SET_WALLET_METHOD, SHOW_MODAL, SHOW_WITHDRAWAL_MODAL } from './types';
import { getCurrentUser } from '../utils/apiPathes';

export const fetchUser = () => async dispatch => {
  try {
    const data = await axios.get(getCurrentUser());
    dispatch({ type: FETCH_USER, payload: data });
  } catch (err) {
    console.warn('Error fetching user', err);
    throw err;
  }
};

export const setWalletMethod = byApiMethod => ({ type: SET_WALLET_METHOD, payload: byApiMethod });

export const closeModal = () => async dispatch => {
  dispatch({ type: SHOW_MODAL, payload: { showModal: false, error: '', contract: '', showLoader: true } });
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({ type: SHOW_WITHDRAWAL_MODAL, payload: { showModal: false, contract: '', showLoader: true } });
};
