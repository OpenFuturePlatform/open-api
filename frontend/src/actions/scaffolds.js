import {FETCH_ETHEREUM_SCAFFOLDS, FETCH_OPEN_SCAFFOLDS, SET_ETHEREUM_SCAFFOLD_SET, SHOW_MODAL} from './types';
import {fetchEthereumShareHoldersFromChain} from './ethereum-share-holders';
import {parseApiError} from '../utils/parseApiError';
import {getEthereumScaffoldsPath, getOpenScaffoldsPath} from '../utils/apiPathes';
import {apiGet, apiPut} from './apiRequest';
import {getApiUsing} from '../selectors/getApiUsing';
import {fetchEthereumScaffoldSummaryFromApi, fetchEthereumScaffoldSummaryFromChain} from './ethereum-scaffold-summary';
import {fetchEthereumScaffoldTransactionsFromApi} from './ethereum-scaffold-transactions';
import {contractVersion} from '../adapters/contract-version';

export const fetchOpenScaffolds = (offset = 0, limit = 10) => async dispatch => {
  const params = { offset, limit };

  try {
    const data = await dispatch(apiGet(getOpenScaffoldsPath(), params));
    dispatch({ type: FETCH_OPEN_SCAFFOLDS, payload: data });
  } catch (err) {
    const error = parseApiError(err);
    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false, showModal: true, error: error.message}
    });
    throw error;
  }
};

export const fetchEthereumScaffolds = (offset = 0, limit = 10) => async dispatch => {
  const params = { offset, limit };

  try {
    const data = await dispatch(apiGet(getEthereumScaffoldsPath(), params));
    const list = data.list.map(scaffold => contractVersion(scaffold.version).adaptScaffold(scaffold));
    const adaptedData = { ...data, list };
    dispatch({ type: FETCH_ETHEREUM_SCAFFOLDS, payload: adaptedData });
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

const fetchEthereumScaffoldItem = address => async dispatch => {
  dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, loading: true } });
  try {
    const scaffoldRaw = await dispatch(apiGet(getEthereumScaffoldsPath(address)));
    const scaffold = contractVersion(scaffoldRaw.version).adaptScaffold(scaffoldRaw);
    const error = '';
    const payload = { address, scaffold, error, loading: false };
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload });
    return scaffold;
  } catch (e) {
    const response = e.response || {};
    const error = `${response.status}: ${response.message || response.statusText}`;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload });
    throw e;
  }
};

export const fetchEthereumScaffoldDetails = scaffoldAddress => async (dispatch, getState) => {
  const state = getState();
  const apiUsing = getApiUsing(state);
  const scaffold = await dispatch(fetchEthereumScaffoldItem(scaffoldAddress));

  if (apiUsing) {
    dispatch(fetchEthereumScaffoldSummaryFromApi(scaffold));
  } else {
    dispatch(fetchEthereumScaffoldSummaryFromChain(scaffold));
    dispatch(fetchEthereumShareHoldersFromChain(scaffold));
  }
  dispatch(fetchEthereumScaffoldTransactionsFromApi(scaffold));
};

export const editEthereumScaffoldByApi = ({ address, version }, fields) => async dispatch => {
  try {
    const serializedScaffold = contractVersion(version).serializeScaffold(fields);
    await dispatch(apiPut(getEthereumScaffoldsPath(address), serializedScaffold));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const editEthereumScaffold = (scaffold, fields) => async dispatch => {
  await dispatch(editEthereumScaffoldByApi(scaffold, fields));

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};
