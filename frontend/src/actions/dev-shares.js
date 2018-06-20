import {getContract} from '../utils/eth';
import web3 from '../utils/web3';
import {SET_DEV_SHARES} from './types';

export const fetchDevShares = (scaffold) => async dispatch => {
  const contract = getContract(scaffold);
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
  console.log(scaffold, shareHolder);
  const contract = new web3.eth.Contract(JSON.parse(scaffold.abi), scaffold.address);
  await contract.methods
    .addShareHolder(shareHolder.address, shareHolder.share)
    .send({from: scaffold.vendorAddress});
  dispatch(fetchDevShares(scaffold));
};

export const removeShareHolder = (scaffold, holderAddress) => async dispatch => {
  const contract = new web3.eth.Contract(JSON.parse(scaffold.abi), scaffold.address);
  await contract.methods.deleteShareHolder(holderAddress).send({from: scaffold.vendorAddress});
  dispatch(fetchDevShares(scaffold));
};
