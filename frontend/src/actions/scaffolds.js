import eth from '../utils/eth';
import axios from 'axios';
import { getContract } from '../utils/eth';
import { SET_SCAFFOLD_SET, FETCH_SCAFFOLDS } from './types';
import { setShareHolders } from './shareHolders';
import { getFromBN } from '../utils/getFromBN';

export const fetchScaffolds = (page = 1, limit = 10) => async dispatch => {
  const offset = (Math.max(page, 1) - 1) * limit;
  const params = { offset, limit };

  try {
    const res = await axios.get('/api/scaffolds', { params });
    dispatch({ type: FETCH_SCAFFOLDS, payload: res.data });
  } catch (err) {
    console.log('Error getting scaffolds', err);
  }
};

const fetchScaffoldItem = address => async dispatch => {
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });
  try {
    const { data: scaffold } = await axios.get(`/api/scaffolds/${address}`);
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
    const { data: summary } = await axios.get(`/api/scaffolds/${address}/summary`);
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

const mapScaffoldSummary = source => {
  const {
    0: description,
    1: fiatAmount,
    2: currency,
    3: conversionAmount,
    4: transactionIndex,
    5: vendorAddress,
    6: tokenBalance
  } = source;
  return {
    description,
    fiatAmount,
    currency,
    conversionAmount: getFromBN(conversionAmount),
    transactionIndex: getFromBN(transactionIndex),
    vendorAddress,
    tokenBalance: getFromBN(tokenBalance)
  };
};
