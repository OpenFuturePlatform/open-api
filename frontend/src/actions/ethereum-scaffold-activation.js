import {fetchEthereumScaffoldDetails} from './scaffolds';
import {openTokenSelectorWeb3} from '../selectors/open-token';
import {apiDelete, apiPost} from './apiRequest';
import {getEthereumScaffoldsPath} from '../utils/apiPathes';
import {getWalletMethod} from '../selectors/getWalletMethod';
import {getWeb3Contract} from '../utils/web3';
import {parseApiError} from '../utils/parseApiError';

export const deactivateEthereumScaffoldByMetaMask = scaffold => async () => {
  const contract = getWeb3Contract(scaffold);
  if (!contract) {
    throw new Error('Install MetaMask to activate Scaffold via Private Wallet');
  }
  return await contract.methods.deactivate().send({ from: scaffold.developerAddress });
};

export const deactivateEthereumScaffoldByApi = scaffold => async dispatch => {
  try {
    await dispatch(apiDelete(getEthereumScaffoldsPath(scaffold.address)));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const deactivateEthereumScaffold = scaffold => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(deactivateEthereumScaffoldByApi(scaffold));
  } else {
    await dispatch(deactivateEthereumScaffoldByMetaMask(scaffold));
  }

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};

export const topUpTokenBalance = (scaffold, amount = '10.0') => async (dispatch, getState) => {
  const state = getState();
  const openToken = openTokenSelectorWeb3(state);
  const tokenDonorWallet = state.ethAccount.account;

  const result = await openToken.methods
    .transfer(scaffold.address, amount * 100000000)
    .send({ from: tokenDonorWallet });

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
  return result;
};

export const activateScaffoldByMetaMask = scaffold => async () => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to activate Scaffold via Private Wallet');
  }

  return await contract.methods.activate().send({ from: scaffold.developerAddress });
};

export const activateEthereumScaffoldByApi = scaffold => async dispatch => {
  try {
    return await dispatch(apiPost(getEthereumScaffoldsPath(scaffold.address), {}));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const activateEthereumScaffold = scaffold => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(activateEthereumScaffoldByApi(scaffold));
  } else {
    await dispatch(activateScaffoldByMetaMask(scaffold));
  }

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};
