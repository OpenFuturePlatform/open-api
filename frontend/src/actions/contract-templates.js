import axios from 'axios';

export const saveTemplate = (values) => async dispatch => {
  return await axios.post('/api/scaffolds/templates', {...values});
};
