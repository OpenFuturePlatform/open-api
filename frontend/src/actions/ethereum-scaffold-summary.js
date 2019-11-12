import {getContract} from '../utils/eth';
import {SET_ETHEREUM_SCAFFOLD_SET} from './types';
import {setEthereumShareHolders} from './ethereum-share-holders';
import {getEthereumScaffoldsSummaryPath} from '../utils/apiPathes';
import {apiGet} from './apiRequest';
import {contractVersion} from '../adapters/contract-version';
import {parseApiError} from '../utils/parseApiError';

export const fetchEthereumScaffoldSummaryFromApi = scaffold => async dispatch => {
  const { address } = scaffold;
  dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, loading: true } });

  try {
    const summaryResponse = await dispatch(apiGet(getEthereumScaffoldsSummaryPath(address)));
    const summary = contractVersion(scaffold.version).serializeScaffoldSummaryByApi(summaryResponse);
    const shareHolders = summary.shareHolders.map(it => ({
      ...it,
      share: it.percent
    }));
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, summary, loading: false } });
    dispatch(setEthereumShareHolders(address, shareHolders));
  } catch (e) {
    const error = parseApiError(e);
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, error: error.message, loading: false } });
    throw error;
  }
};

export const fetchEthereumScaffoldSummaryFromChain = scaffold => async dispatch => {
  const { address } = scaffold;
  dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, loading: true } });
  const contract = getContract(scaffold);

  if (!contract) {
    const error = 'To view Scaffold Summary you need to install MetaMask and refresh page';
    const payload = { address, loading: false, error };
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload });
    return;
  }

  try {
    const summaryResponse = await contract.getScaffoldSummary();
    const summary = contractVersion(scaffold.version).serializeScaffoldSummaryByMetaMask(summaryResponse);
    const payload = { address, summary, loading: false };
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload });
  } catch (e) {
    const error = e;
    const payload = { address, error, loading: false };
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload });
    throw e;
  }
};
