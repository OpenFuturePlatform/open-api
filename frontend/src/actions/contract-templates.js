import {LOAD_ETHEREUM_SCAFFOLD_FIELDS, SET_ETHEREUM_SCAFFOLD_TEMPLATES} from './types';
import {sortBy} from 'lodash';
import {getEthereumTemplatesPath} from '../utils/apiPathes';
import {apiGet, apiPost} from './apiRequest';

export const setTemplate = fields => async dispatch => {
  dispatch({ type: LOAD_ETHEREUM_SCAFFOLD_FIELDS, payload: fields });
};

export const fetchTemplates = () => async dispatch => {
  const data = await dispatch(apiGet(getEthereumTemplatesPath()));
  dispatch({ type: SET_ETHEREUM_SCAFFOLD_TEMPLATES, payload: sortBy(data, 'name') });
};

export const saveTemplate = values => async dispatch => {
  const result = await dispatch(apiPost(getEthereumTemplatesPath(), { ...values }));
  dispatch(fetchTemplates());
  return result;
};
