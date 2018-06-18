import axios from 'axios';
import {
  FETCH_ONCHAIN_SCAFFOLD_SUMMARY,
  FETCH_SCAFFOLDS,
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

export const fetchScaffolds = (page = 1, limit = 10) => async dispatch => {
  const offset = (Math.max(page, 1) - 1) * limit;
  const params = {offset, limit};

  try {
    const res = await axios.get('/api/scaffolds', {params});
    dispatch({type: FETCH_SCAFFOLDS, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const fetchScaffoldItem = (scaffoldAddress) => async dispatch => {
  try {
    const res = await axios.get(`/api/scaffolds/${scaffoldAddress}/summary`);
    dispatch({type: FETCH_ONCHAIN_SCAFFOLD_SUMMARY, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const closeModal = () => async dispatch => {
  dispatch({type: SHOW_MODAL, payload: {showModal: false, error: '', contract: '', showLoader: true}});
};

export const closeWithdrawalModal = () => async dispatch => {
  dispatch({type: SHOW_WITHDRAWAL_MODAL, payload: {showModal: false, contract: '', showLoader: true}});
};
