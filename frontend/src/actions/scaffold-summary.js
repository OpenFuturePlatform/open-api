import { getContract } from '../utils/eth';
import { SET_SCAFFOLD_SET } from './types';
import { setShareHolders } from './share-holders';
import { getScaffoldsSummaryPath } from '../utils/apiPathes';
import { apiGet } from './apiRequest';
import { contractVersion } from '../adapters/contract-version';

export const fetchScaffoldSummaryFromApi = scaffold => async dispatch => {
  const { address } = scaffold;
  dispatch({ type: SET_SCAFFOLD_SET, payload: { address, loading: true } });

  try {
    const summary = await dispatch(apiGet(getScaffoldsSummaryPath(address)));
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
    const summary = contractVersion(scaffold.version).serializeScaffoldSummary(summaryResponse);
    const payload = { address, summary, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
  } catch (e) {
    const error = e;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_SCAFFOLD_SET, payload });
    throw e;
  }
};
