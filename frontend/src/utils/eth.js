import Eth from 'ethjs';

let eth;

if (typeof window !== 'undefined' && typeof window.web3 !== 'undefined' && window.web3.currentProvider.isMetaMask) {
  eth = new Eth(window.web3.currentProvider);
} else {
  eth = null;
}

export const getContract = ({abi, address}) => eth.contract(JSON.parse(abi)).at(address);

export default eth;
