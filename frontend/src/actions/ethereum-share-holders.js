import {getContract} from '../utils/eth';
import {getWeb3Contract} from '../utils/web3';
import {SET_ETHEREUM_SCAFFOLD_SET, SET_ETHEREUM_SCAFFOLD_SHARE_HOLDERS} from './types';
import {getWalletMethod} from '../selectors/getWalletMethod';
import {fetchEthereumScaffoldDetails} from './scaffolds';
import {parseApiError} from '../utils/parseApiError';
import {getEthereumShareHoldersPath} from '../utils/apiPathes';
import {apiDelete, apiPost, apiPut} from './apiRequest';
import {toChecksumAddress} from '../utils/toChecksumAddress';

export const fetchEthereumShareHoldersFromChain = scaffold => async dispatch => {
  const address = scaffold.address;
  const contract = getContract(scaffold);

  if (!contract) {
    return; // no errors. shareHolders can be fetch form back-end
  }

  try {
    const shareHolderCountResult = await contract.getShareHolderCount();
    const shareHolderCount = shareHolderCountResult[0].toString(10);
    const shareHolders = [];

    for (let i = 0; i < shareHolderCount; i++) {
      const shareHolderResult = await contract.getShareHolderAtIndex(i);
      const address = toChecksumAddress(shareHolderResult[0]);
      const shareResult = await contract.getHoldersShare(address);
      const share = shareResult[0].toString(10);
      shareHolders.push({ address, share });
    }

    dispatch({
      type: SET_ETHEREUM_SCAFFOLD_SHARE_HOLDERS,
      payload: { address, shareHolders }
    });
  } catch (e) {
    const error = e.message;
    dispatch({ type: SET_ETHEREUM_SCAFFOLD_SET, payload: { address, error } });
  }
};

export const setEthereumShareHolders = (address, shareHolders) => async dispatch => {
  dispatch({
    type: SET_ETHEREUM_SCAFFOLD_SHARE_HOLDERS,
    payload: { address, shareHolders }
  });
};

export const addEthereumShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to add Share Holder via Private Wallet');
  }

  return await contract.methods
    .addShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.developerAddress });
};

export const addEthereumShareHolderByApi = (scaffold, { address, share }) => async dispatch => {
  try {
    return await dispatch(
      apiPost(getEthereumShareHoldersPath(scaffold.address), {
        address,
        percent: share
      })
    );
  } catch (e) {
    throw parseApiError(e);
  }
};

export const addEthereumShareHolder = (scaffold, shareHolder) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(addEthereumShareHolderByApi(scaffold, shareHolder));
  } else {
    await dispatch(addEthereumShareHolderByMetaMask(scaffold, shareHolder));
  }

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};

export const editEthereumShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to edit Share Holder via Private Wallet');
  }

  return await contract.methods
    .editShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.developerAddress });
};

export const editEthereumShareHolderByApi = (scaffold, { address, share }) => async dispatch => {
  try {
    return await dispatch(
      apiPut(getEthereumShareHoldersPath(scaffold.address, address), {
        address,
        percent: share
      })
    );
  } catch (e) {
    throw parseApiError(e);
  }
};

export const editEthereumShareHolder = (scaffold, shareHolder) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(editEthereumShareHolderByApi(scaffold, shareHolder));
  } else {
    await dispatch(editEthereumShareHolderByMetaMask(scaffold, shareHolder));
  }

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};

export const removeEthereumShareHolderByMetaMask = (scaffold, holderAddress) => async () => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to delete Share Holder via Private Wallet');
  }

  return contract.methods.deleteShareHolder(holderAddress).send({ from: scaffold.developerAddress });
};

export const removeEthereumShareHolderByApi = (scaffold, holderAddress) => async dispatch => {
  try {
    // it cuts body of request
    // return await axios.delete(`/api/scaffolds/${scaffold.address}/holders`, {address: holderAddress});
    return await dispatch(apiDelete(getEthereumShareHoldersPath(scaffold.address, holderAddress)));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const removeEthereumShareHolder = (scaffold, holderAddress) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(removeEthereumShareHolderByApi(scaffold, holderAddress));
  } else {
    await dispatch(removeEthereumShareHolderByMetaMask(scaffold, holderAddress));
  }

  dispatch(fetchEthereumScaffoldDetails(scaffold.address));
};
