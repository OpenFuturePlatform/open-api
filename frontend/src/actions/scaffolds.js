import axios from 'axios';
import {
  FETCH_ONCHAIN_SCAFFOLD_SUMMARY,
  FETCH_SCAFFOLDS,
} from './types';

export const fetchScaffolds = (page = 1, limit = 10) => async dispatch => {
  const offset = (Math.max(page, 1) - 1) * limit;
  const params = {offset, limit};

  try {
    const res = await axios.get('/api/scaffolds', {params});
    dispatch({type: FETCH_SCAFFOLDS, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

export const fetchScaffoldItemFromApi = (scaffoldAddress) => async dispatch => {
  try {
    const res = await axios.get(`/api/scaffolds/${scaffoldAddress}/summary`);
    dispatch({type: FETCH_ONCHAIN_SCAFFOLD_SUMMARY, payload: res.data});
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};