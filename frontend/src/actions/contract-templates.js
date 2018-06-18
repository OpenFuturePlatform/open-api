import axios from 'axios';

export const saveTemplate = (values) => async dispatch => {
  const result = await axios.post('/api/scaffolds/templates', {...values, name: 'hello 2'});

  console.log(result);
};
