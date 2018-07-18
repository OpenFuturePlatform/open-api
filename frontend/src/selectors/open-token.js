import eth from '../utils/eth';
import { OPEN_ABI } from '../const/open';
import { getWeb3Contract } from '../utils/web3';

export const openTokenSelectorEth = state => {
  const openAddress = state.globalProperties.openTokenAddress;
  return eth ? eth.contract(OPEN_ABI).at(openAddress) : null;
};

export const openTokenSelectorWeb3 = state => {
  const openAddress = state.globalProperties.openTokenAddress;
  const parsedAbi = OPEN_ABI;
  const address = openAddress;
  return getWeb3Contract({ parsedAbi, address });
};
