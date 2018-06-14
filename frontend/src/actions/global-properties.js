import axios from 'axios';
import {SET_GLOBAL_PROPERTIES} from "./types";

export const fetchGlobalProperties = () => async dispatch => {
  try {
    const response = await axios.get('/api/properties');
    dispatch({type: SET_GLOBAL_PROPERTIES, payload: response.data});
  } catch (e) {
    console.warn('fetching global properties error: ', e);
  }
};
