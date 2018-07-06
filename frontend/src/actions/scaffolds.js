import eth from '../utils/eth';
import axios from 'axios';
import { getContract } from '../utils/eth';
import { SET_SCAFFOLD_SET, FETCH_SCAFFOLDS } from './types';
import { setShareHolders } from './shareHolders';
import { getFromBN } from '../utils/getFromBN';
import { parseApiError } from '../utils/parseApiError';
import { getScaffoldsPath, getScaffoldsSummaryPath } from '../utils/apiPathes';
import { adaptScaffold, serializeScaffold } from '../utils/scaffold-adapter';

export const fetchScaffolds = (offset = 0, limit = 10) => async dispatch => {
  const params = { offset, limit };

  try {
    const { data } = await axios.get(getScaffoldsPath(), { params });
    const adaptedData = { ...data, list: data.list.map(adaptScaffold) };
    dispatch({ type: FETCH_SCAFFOLDS, payload: adaptedData });
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

const fetchScaffoldItem = address => async dispatch => {
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });
  try {
    const { data: scaffoldRaw } = await axios.get(getScaffoldsPath(address));
    const scaffold = adaptScaffold(scaffoldRaw);
    const error = '';
    const payload = { address, scaffold, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    return scaffold;
  } catch (e) {
    const error = `${e.response.status}: ${e.response.message || e.response.statusText}`;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    throw e;
  }
};

export const fetchScaffoldSummaryFromChain = scaffold => async dispatch => {
  const { address } = scaffold;
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });
  const contract = getContract(scaffold);

  if (!contract) {
    const error = 'To view Scaffold Summary you need to install MetaMask and refresh page';
    const payload = { address, loading: false, error };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    return;
  }

  try {
    const summaryResponse = await contract.getScaffoldSummary();
    const summary = mapScaffoldSummary(summaryResponse);
    const payload = { address, summary, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
  } catch (e) {
    const error = e;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    throw e;
  }
};

export const fetchScaffoldSummaryFromApi = scaffold => async dispatch => {
  const { address } = scaffold;
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });

  try {
    const { data: summary } = await axios.get(getScaffoldsSummaryPath(address));
    const shareHolders = summary.shareHolders.map(it => ({
      ...it,
      share: it.percent
    }));
    dispatch({ type: SET_SCAFFOLD_SET, payload: { address, summary, loading: false } });
    dispatch(setShareHolders(address, shareHolders));
  } catch (e) {
    const error = `${e.response.status}: ${e.response.message || e.response.statusText}`;
    dispatch({ type: SET_SCAFFOLD_SET, payload: { address, error, loading: false } });
    throw e;
  }
};

export const fetchScaffoldSummary = scaffoldAddress => async (dispatch, getState) => {
  const {
    auth: { isApiAllowed }
  } = getState();
  const scaffold = await dispatch(fetchScaffoldItem(scaffoldAddress));

  if (isApiAllowed && !eth) {
    console.log('>> back fetch');
    await dispatch(fetchScaffoldSummaryFromApi(scaffold));
  } else {
    console.log('>> block chain fetch');
    await dispatch(fetchScaffoldSummaryFromChain(scaffold));
  }
};

export const editScaffoldByApi = ({ address }, fields) => async () => {
  try {
    const serializedScaffold = serializeScaffold(fields);
    await axios.put(getScaffoldsPath(address), serializedScaffold);
  } catch (e) {
    throw parseApiError(e);
  }
};

export const editScaffold = (scaffold, fields) => async dispatch => {
  await dispatch(editScaffoldByApi(scaffold, fields));

  dispatch(fetchScaffoldSummary(scaffold.address));
};

const mapScaffoldSummary = source => {
  const {
    0: fiatAmount,
    1: currency,
    2: conversionAmount,
    3: transactionIndex,
    4: developerAddress,
    5: tokenBalance
  } = source;
  return {
    fiatAmount,
    currency,
    conversionAmount: getFromBN(conversionAmount),
    transactionIndex: getFromBN(transactionIndex),
    developerAddress,
    tokenBalance: getFromBN(tokenBalance) / 100000000
  };
};
