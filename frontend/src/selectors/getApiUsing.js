import { getApiAllowed } from './getApiAllowed';
import { getMetaMaskAllowed } from './getMetaMaskError';

export const getApiUsing = state => {
  const isApiAllowed = getApiAllowed(state);
  const isMetaMaskAllowed = getMetaMaskAllowed(state);
  return isApiAllowed && !isMetaMaskAllowed;
};
