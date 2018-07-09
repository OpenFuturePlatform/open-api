import { SET_GLOBAL_PROPERTIES } from './types';
import { apiGet } from './apiRequest';
import { getGlobalPropertiesPath } from '../utils/apiPathes';

export const fetchGlobalProperties = () => async dispatch => {
  const data = await dispatch(apiGet(getGlobalPropertiesPath()));
  dispatch({ type: SET_GLOBAL_PROPERTIES, payload: data });
};
