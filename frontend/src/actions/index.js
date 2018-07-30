import { FETCH_USER, SET_WALLET_METHOD, SHOW_MODAL, SHOW_WITHDRAWAL_MODAL, SET_AUTH } from './types';
import { getCurrentUserPath } from '../utils/apiPathes';
import { apiGet } from './apiRequest';

export const setAuthorized = isAuthorized => ({ type: SET_AUTH, payload: isAuthorized });

export const fetchUser = () => async dispatch => {
  const data = await dispatch(apiGet(getCurrentUserPath()));
  dispatch({ type: FETCH_USER, payload: data, isLoading: false });
  dispatch(setAuthorized(true));
};

export const setWalletMethod = byApiMethod => ({ type: SET_WALLET_METHOD, payload: byApiMethod });

export const closeModal = () => async dispatch => {
  dispatch({ type: SHOW_MODAL, payload: { showModal: false, error: '', contract: '', showLoader: true } });
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({ type: SHOW_WITHDRAWAL_MODAL, payload: { showModal: false, contract: '', showLoader: true } });
};
