import axios from 'axios';
import { setAuthorized } from '.';
import { parseApiError } from '../utils/parseApiError';

export const request = (method, url, data, params) => async dispatch => {
  try {
    const { data: response, headers } = await axios({
      method,
      url,
      params,
      data
    });

    if (!headers['content-type'].includes('application/json')) {
      dispatch(setAuthorized(false));
      const error = { message: 'Api request finished without application/json content-type' };
      throw error;
    }

    return response;
  } catch (e) {
    const response = e.response || {};
    const status = response.status;

    if (status === 401 || status === 403) {
      dispatch(setAuthorized(false));
    }

    throw parseApiError(e);
  }
};

export const apiGet = (url, params) => async dispatch => await dispatch(request('get', url, {}, params));

export const apiPost = (url, data) => async dispatch => await dispatch(request('post', url, data, {}));

export const apiPatch = (url, data) => async dispatch => await dispatch(request('patch', url, data, {}));

export const apiPut = (url, data) => async dispatch => await dispatch(request('put', url, data, {}));

export const apiDelete = (url, data) => async dispatch => await dispatch(request('delete', url, data));
