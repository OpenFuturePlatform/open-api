import eth from '../utils/eth';
import { getTargetNetwork } from './getTargetNetwork';
import { getCurrentNetworkId } from './getCurrentNetworkId';
import { MIN_CONTRACT_DEPOSIT } from '../const';
import { t } from '../utils/messageTexts';

export const metaMaskExistsError = () => (!eth ? t('MM not found') : '');

export const getCriticalMetaMaskError = state => {
  if (metaMaskExistsError()) {
    return metaMaskExistsError();
  }

  const activeNetworkId = getCurrentNetworkId(state);
  const targetNetwork = getTargetNetwork(state);

  if (Number(activeNetworkId) !== Number(targetNetwork.id)) {
    return t('choose target network', targetNetwork.name);
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
    return t('log in to MM');
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
    return t('MM have no min tokens', MIN_CONTRACT_DEPOSIT);
  }
  return '';
};
