import axios from 'axios';
import {LOAD_SCAFFOLD_FIELDS, SET_SCAFFOLD_TEMPLATES} from './types';
import {sortBy} from 'lodash';

export const setTemplate = (fields) => async dispatch => {
  dispatch({type: LOAD_SCAFFOLD_FIELDS, payload: fields});
};

export const fetchTemplates = () => async dispatch => {
  const result = await axios.get('/api/scaffolds/templates');
  dispatch({type: SET_SCAFFOLD_TEMPLATES, payload: sortBy(result.data, 'name')})
};

export const saveTemplate = (values) => async dispatch => {
  const result = await axios.post('/api/scaffolds/templates', {...values});
  dispatch(fetchTemplates());
  return result;
};
