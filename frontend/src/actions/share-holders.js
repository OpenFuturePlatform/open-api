import { getContract } from '../utils/eth';
import { getWeb3Contract } from '../utils/web3';
import { SET_SCAFFOLD_SET, SET_SCAFFOLD_SHARE_HOLDERS } from './types';
import { getWalletMethod } from '../selectors/getWalletMethod';
import { fetchScaffoldDetails } from './scaffolds';
import { parseApiError } from '../utils/parseApiError';
import { getShareHoldersPath } from '../utils/apiPathes';
import { apiPost, apiPut, apiDelete } from './apiRequest';
import { toChecksumAddress } from '../utils/toChecksumAddress';

export const fetchShareHoldersFromChain = scaffold => async dispatch => {
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
      type: SET_SCAFFOLD_SHARE_HOLDERS,
      payload: { address, shareHolders }
    });
  } catch (e) {
    const error = e.message;
    dispatch({ type: SET_SCAFFOLD_SET, payload: { address, error } });
  }
};

export const setShareHolders = (address, shareHolders) => async dispatch => {
  dispatch({
    type: SET_SCAFFOLD_SHARE_HOLDERS,
    payload: { address, shareHolders }
  });
};

export const addShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to add Share Holder via Private Wallet');
  }

  return await contract.methods
    .addShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.developerAddress });
};

export const addShareHolderByApi = (scaffold, { address, share }) => async dispatch => {
  try {
    return await dispatch(
      apiPost(getShareHoldersPath(scaffold.address), {
        address,
        percent: share
      })
    );
  } catch (e) {
    throw parseApiError(e);
  }
};

export const addShareHolder = (scaffold, shareHolder) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(addShareHolderByApi(scaffold, shareHolder));
  } else {
    await dispatch(addShareHolderByMetaMask(scaffold, shareHolder));
  }

  dispatch(fetchScaffoldDetails(scaffold.address));
};

export const editShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to edit Share Holder via Private Wallet');
  }

  return await contract.methods
    .editShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.developerAddress });
};

export const editShareHolderByApi = (scaffold, { address, share }) => async dispatch => {
  try {
    return await dispatch(
      apiPut(getShareHoldersPath(scaffold.address, address), {
        address,
        percent: share
      })
    );
  } catch (e) {
    throw parseApiError(e);
  }
};

export const editShareHolder = (scaffold, shareHolder) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(editShareHolderByApi(scaffold, shareHolder));
  } else {
    await dispatch(editShareHolderByMetaMask(scaffold, shareHolder));
  }

  dispatch(fetchScaffoldDetails(scaffold.address));
};

export const removeShareHolderByMetaMask = (scaffold, holderAddress) => async () => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to delete Share Holder via Private Wallet');
  }

  return contract.methods.deleteShareHolder(holderAddress).send({ from: scaffold.developerAddress });
};

export const removeShareHolderByApi = (scaffold, holderAddress) => async dispatch => {
  try {
    // it cuts body of request
    // return await axios.delete(`/api/scaffolds/${scaffold.address}/holders`, {address: holderAddress});
    return await dispatch(apiDelete(getShareHoldersPath(scaffold.address, holderAddress)));
  } catch (e) {
    throw parseApiError(e);
  }
};

export const removeShareHolder = (scaffold, holderAddress) => async (dispatch, getState) => {
  const state = getState();
  const { byApiMethod } = getWalletMethod(state);

  if (byApiMethod) {
    await dispatch(removeShareHolderByApi(scaffold, holderAddress));
  } else {
    await dispatch(removeShareHolderByMetaMask(scaffold, holderAddress));
  }

  dispatch(fetchScaffoldDetails(scaffold.address));
};
