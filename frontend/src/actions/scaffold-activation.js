import { fetchScaffoldDetails } from './scaffolds';
import { openTokenSelectorWeb3 } from '../selectors/open-token';
import { apiPost, apiDelete } from './apiRequest';
import { getScaffoldsPath } from '../utils/apiPathes';
import { getWalletMethod } from '../selectors/getWalletMethod';
import { getWeb3Contract } from '../utils/web3';
import { parseApiError } from '../utils/parseApiError';

export const deactivateScaffoldByMetaMask = scaffold => async () => {
  const contract = getWeb3Contract(scaffold);
  if (!contract) {
    throw new Error('Install MetaMask to activate Scaffold via Private Wallet');
  }
  return await contract.methods.deactivate().send({ from: scaffold.developerAddress });
};

export const deactivateScaffoldByApi = scaffold => async dispatch => {
  try {
    await dispatch(apiDelete(getScaffoldsPath(scaffold.address)));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const deactivateScaffold = scaffold => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(deactivateScaffoldByApi(scaffold));
  } else {
    await dispatch(deactivateScaffoldByMetaMask(scaffold));
  }

  dispatch(fetchScaffoldDetails(scaffold.address));
};

export const topUpTokenBalance = (scaffold, amount = '10.0') => async (dispatch, getState) => {
  const state = getState();
  const openToken = openTokenSelectorWeb3(state);
  const tokenDonorWallet = state.ethAccount.account;

  const result = await openToken.methods
    .transfer(scaffold.address, amount * 100000000)
    .send({ from: tokenDonorWallet });

  dispatch(fetchScaffoldDetails(scaffold.address));
  return result;
};

export const activateScaffoldByMetaMask = scaffold => async () => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to activate Scaffold via Private Wallet');
  }

  return await contract.methods.activate().send({ from: scaffold.developerAddress });
};

export const activateScaffoldByApi = scaffold => async dispatch => {
  try {
    return await dispatch(apiPost(getScaffoldsPath(scaffold.address), {}));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const activateScaffold = scaffold => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(activateScaffoldByApi(scaffold));
  } else {
    await dispatch(activateScaffoldByMetaMask(scaffold));
  }

  dispatch(fetchScaffoldDetails(scaffold.address));
};
