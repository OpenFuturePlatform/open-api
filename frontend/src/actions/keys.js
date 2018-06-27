import axios from 'axios';
import { FETCH_KEYS } from './types';

export const fetchKeys = () => async dispatch => {
  const { data: keys } = await axios.get('/api/keys');
  dispatch({ type: FETCH_KEYS, payload: keys });
  return keys;
};

export const removeKey = value => async dispatch => {
  await axios.delete(`/api/keys/${value}`);
  dispatch(fetchKeys());
};
