import eth from '../utils/eth';
import {getTargetNetwork} from './getTargetNetwork';
import {getCurrentNetworkId} from './getCurrentNetworkId';

export const metaMaskExistsError = () => !eth ? 'Install MetaMask and refresh page' : '';

export const getMetaMaskError = state => {
  if (metaMaskExistsError()) {
    return metaMaskExistsError();
  }

  const activeNetworkId = getCurrentNetworkId(state);
  if (!activeNetworkId) {
    return 'Log in to MetaMask';
  }

  const targetNetwork = getTargetNetwork(state);
  return activeNetworkId !== targetNetwork.id ? `Choose ${targetNetwork.name} MetaMask network` : '';
};
