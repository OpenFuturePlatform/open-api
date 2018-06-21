import {getContract} from '../utils/eth';
import {getWeb3Contract} from '../utils/web3';
import {SET_DEV_SHARES} from './types';

export const fetchShareHolders = (scaffold) => async dispatch => {
  const contract = getContract(scaffold);

  if (!contract) {
    return;
  }

  const shareHolderCountResult = await contract.getShareHolderCount();
  const shareHolderCount = shareHolderCountResult[0].toString(10);
  const shareHolders = [];

  for (let i = 0; i < shareHolderCount; i++) {
    const shareHolderResult = await contract.getShareHolderAtIndex(i);
    const address = shareHolderResult[0];
    const shareResult = await contract.getHoldersShare(address);
    const share = shareResult[0].toString(10);
    shareHolders.push({address, share});
  }

  dispatch({type: SET_DEV_SHARES, payload: shareHolders});
};

export const addShareHolder = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);
  await contract.methods
    .addShareHolder(shareHolder.address, shareHolder.share)
    .send({from: scaffold.vendorAddress});
  dispatch(fetchShareHolders(scaffold));
};

export const editShareHolder = (scaffold, shareHolder) => async dispatch => {
  const contract = getWeb3Contract(scaffold);
  await contract.methods
    .editShareHolder(shareHolder.address, shareHolder.share)
    .send({from: scaffold.vendorAddress});
  dispatch(fetchShareHolders(scaffold));
};

export const removeShareHolder = (scaffold, holderAddress) => async dispatch => {
  const contract = getWeb3Contract(scaffold);
  await contract.methods.deleteShareHolder(holderAddress).send({from: scaffold.vendorAddress});
  dispatch(fetchShareHolders(scaffold));
};
