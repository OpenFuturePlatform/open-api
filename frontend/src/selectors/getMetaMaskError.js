import eth from '../utils/eth';
import { getTargetNetwork } from './getTargetNetwork';
import { getCurrentNetworkId } from './getCurrentNetworkId';
import { MIN_CONTRACT_DEPOSIT } from '../const';

export const metaMaskExistsError = () => (!eth ? 'Install MetaMask and refresh page' : '');

export const getCriticalMetaMaskError = state => {
  if (metaMaskExistsError()) {
    return metaMaskExistsError();
  }

  const activeNetworkId = getCurrentNetworkId(state);
  const targetNetwork = getTargetNetwork(state);

  if (Number(activeNetworkId) !== Number(targetNetwork.id)) {
    return `Choose ${targetNetwork.name}`;
  }

  return '';
};

export const getMetaMaskAllowed = state => !getCriticalMetaMaskError(state);

export const getMetaMaskErrorMessage = state => {
  const criticalError = getCriticalMetaMaskError(state);
  if (criticalError) {
    return criticalError;
  }

  if (!state.ethAccount.account) {
    return 'Log in to MetaMask';
  }

  return '';
};

export const validateMMTokenBalance = state => {
  const metaMaskError = getMetaMaskErrorMessage(state);
  const metaMaskTokenBalance = state.ethAccount.tokenBalance;
  if (metaMaskError) {
    return metaMaskError;
  }
  if (metaMaskTokenBalance < MIN_CONTRACT_DEPOSIT) {
    return `MetaMask account have no OPEN Tokens. Select account with ${MIN_CONTRACT_DEPOSIT} OPEN tokens or top up the balance.`;
  }
  return '';
};
