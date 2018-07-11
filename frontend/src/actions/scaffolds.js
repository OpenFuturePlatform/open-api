import { SET_SCAFFOLD_SET, FETCH_SCAFFOLDS } from './types';
import { fetchShareHoldersFromChain } from './share-holders';
import { parseApiError } from '../utils/parseApiError';
import { getScaffoldsPath } from '../utils/apiPathes';
import { adaptScaffold, serializeScaffold } from '../utils/scaffold-adapter';
import { apiGet, apiPut } from './apiRequest';
import { getApiUsing } from '../selectors/getApiUsing';
import { fetchScaffoldSummaryFromApi, fetchScaffoldSummaryFromChain } from './scaffold-summary';
import { fetchScaffoldTransactionsFromApi, fetchScaffoldTransactionsFromChain } from './scaffold-transactions';

export const fetchScaffolds = (offset = 0, limit = 10) => async dispatch => {
  const params = { offset, limit };

  try {
    const data = await dispatch(apiGet(getScaffoldsPath(), params));
    const adaptedData = { ...data, list: data.list.map(adaptScaffold) };
    dispatch({ type: FETCH_SCAFFOLDS, payload: adaptedData });
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

const fetchScaffoldItem = address => async dispatch => {
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });
  try {
    const scaffoldRaw = await dispatch(apiGet(getScaffoldsPath(address)));
    const scaffold = adaptScaffold(scaffoldRaw);
    const error = '';
    const payload = { address, scaffold, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    return scaffold;
  } catch (e) {
    const response = e.response || {};
    const error = `${response.status}: ${response.message || response.statusText}`;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    throw e;
  }
};

export const fetchScaffoldDetails = scaffoldAddress => async (dispatch, getState) => {
  const state = getState();
  const apiUsing = getApiUsing(state);
  const scaffold = await dispatch(fetchScaffoldItem(scaffoldAddress));

  if (apiUsing) {
    console.time('>> back fetch');
    await dispatch(fetchScaffoldSummaryFromApi(scaffold));
    // await dispatch(fetchScaffoldTransactionsFromApi(scaffold));
    console.timeEnd('>> back fetch');
  } else {
    console.time('>> block chain fetch');
    await dispatch(fetchScaffoldSummaryFromChain(scaffold));
    await dispatch(fetchShareHoldersFromChain(scaffold));
    // await dispatch(fetchScaffoldTransactionsFromChain(scaffold));
    console.timeEnd('>> block chain fetch');
  }
  await dispatch(fetchScaffoldTransactionsFromChain(scaffold));
  await dispatch(fetchScaffoldTransactionsFromApi(scaffold));
};

export const editScaffoldByApi = ({ address }, fields) => async dispatch => {
  try {
    const serializedScaffold = serializeScaffold(fields);
    await dispatch(apiPut(getScaffoldsPath(address), serializedScaffold));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const editScaffold = (scaffold, fields) => async dispatch => {
  await dispatch(editScaffoldByApi(scaffold, fields));

  dispatch(fetchScaffoldDetails(scaffold.address));
};
