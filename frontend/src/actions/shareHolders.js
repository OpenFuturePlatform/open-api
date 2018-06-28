import axios from 'axios';
import eth, { getContract } from '../utils/eth';
import { getWeb3Contract } from '../utils/web3';
import { SET_SCAFFOLD_SET, SET_SCAFFOLD_SHARE_HOLDERS } from './types';
import { getWalletMethod } from '../selectors/getWalletMethod';
import { fetchScaffoldSummary } from './scaffolds';
import { parseApiError } from '../utils/parseApiError';

const handleApiError = e => {
  const status = e.response.status;
  const message = e.response.data.message || e.response.statusText;
  const error = `${status}: ${message}`;
  throw new Error(error);
};

export const fetchShareHolders = scaffold => async dispatch => {
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
      const address = shareHolderResult[0];
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

export const refreshShareHolders = scaffold => async dispatch => {
  if (eth) {
    dispatch(fetchShareHolders(scaffold));
  } else {
    dispatch(fetchScaffoldSummary(scaffold.address));
  }
};

export const addShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to add Share Holder via Private Wallet');
  }

  return await contract.methods
    .addShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.vendorAddress });
};

export const addShareHolderByApi = (scaffold, { address, share }) => async () => {
  try {
    return await axios.post(`/api/scaffolds/${scaffold.address}/holders`, {
      address,
      percent: share
    });
  } catch (e) {
    const message = parseApiError(e);
    throw new Error(message);
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

  dispatch(refreshShareHolders(scaffold));
};

export const editShareHolderByMetaMask = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to edit Share Holder via Private Wallet');
  }

  return await contract.methods
    .editShareHolder(shareHolder.address, shareHolder.share)
    .send({ from: scaffold.vendorAddress });
};

export const editShareHolderByApi = (scaffold, { address, share }) => async () => {
  try {
    return await axios.put(`/api/scaffolds/${scaffold.address}/holders`, {
      address,
      percent: share
    });
  } catch (e) {
    const message = parseApiError(e);
    throw new Error(message);
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

  dispatch(refreshShareHolders(scaffold));
};

export const removeShareHolderByMetaMask = (scaffold, holderAddress) => async () => {
  const contract = getWeb3Contract(scaffold);

  if (!contract) {
    throw new Error('Install MetaMask to delete Share Holder via Private Wallet');
  }

  return contract.methods.deleteShareHolder(holderAddress).send({ from: scaffold.vendorAddress });
};

export const removeShareHolderByApi = (scaffold, holderAddress) => async () => {
  try {
    // it cuts body of request
    // return await axios.delete(`/api/scaffolds/${scaffold.address}/holders`, {address: holderAddress});
    return await axios({
      method: 'delete',
      url: `/api/scaffolds/${scaffold.address}/holders`,
      data: { address: holderAddress }
    });
  } catch (e) {
    const message = parseApiError(e);
    throw new Error(message);
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

  dispatch(refreshShareHolders(scaffold));
};
