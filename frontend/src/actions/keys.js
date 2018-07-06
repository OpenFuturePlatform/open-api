import axios from 'axios';
import { FETCH_KEYS } from './types';
import { parseApiError } from '../utils/parseApiError';

export const fetchKeys = () => async dispatch => {
  try {
    const { data: keys } = await axios.get('/api/keys');
    dispatch({ type: FETCH_KEYS, payload: keys });
    return keys;
  } catch (e) {
    throw parseApiError(e);
  }
};

export const generateKey = (key = {}) => async dispatch => {
  try {
    await axios.post('/api/keys', key);
    dispatch(fetchKeys());
  } catch (e) {
    throw parseApiError(e);
  }
};

export const removeKey = value => async dispatch => {
  try {
    await axios.delete(`/api/keys/${value}`);
    dispatch(fetchKeys());
  } catch (e) {
    throw parseApiError(e);
  }
};
