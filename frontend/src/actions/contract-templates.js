import { LOAD_SCAFFOLD_FIELDS, SET_SCAFFOLD_TEMPLATES } from './types';
import { sortBy } from 'lodash';
import { getTemplatesPath } from '../utils/apiPathes';
import { apiGet, apiPost } from './apiRequest';

export const setTemplate = fields => async dispatch => {
  dispatch({ type: LOAD_SCAFFOLD_FIELDS, payload: fields });
};

export const fetchTemplates = () => async dispatch => {
  const data = await dispatch(apiGet(getTemplatesPath()));
  dispatch({ type: SET_SCAFFOLD_TEMPLATES, payload: sortBy(data, 'name') });
};

export const saveTemplate = values => async dispatch => {
  const result = await dispatch(apiPost(getTemplatesPath(), { ...values }));
  dispatch(fetchTemplates());
  return result;
};
