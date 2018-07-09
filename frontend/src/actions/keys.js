import { FETCH_KEYS } from './types';
import { parseApiError } from '../utils/parseApiError';
import { apiGet, apiPost, apiDelete } from './apiRequest';
import { getKeysPath } from '../utils/apiPathes';

export const fetchKeys = () => async dispatch => {
  const keys = await dispatch(apiGet(getKeysPath()));
  dispatch({ type: FETCH_KEYS, payload: keys });
  return keys;
};

export const generateKey = (key = {}) => async dispatch => {
  await dispatch(apiPost(getKeysPath(), key));
  dispatch(fetchKeys());
};

export const removeKey = value => async dispatch => {
  try {
    await dispatch(apiDelete(getKeysPath(value)));
    dispatch(fetchKeys());
  } catch (e) {
    throw parseApiError(e);
  }
};
