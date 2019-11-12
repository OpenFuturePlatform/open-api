import web3 from '../utils/web3';

export const toChecksumAddress = address => {
  try {
    return web3.utils.toChecksumAddress(address);
  } catch (e) {
    throw e;
  }
};
