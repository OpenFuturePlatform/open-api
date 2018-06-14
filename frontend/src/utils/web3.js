import Web3 from 'web3';

let web3;

if (typeof window !== 'undefined' && typeof window.web3 !== 'undefined' && window.web3.currentProvider.isMetaMask) {
  web3 = new Web3(window.web3.currentProvider);
} else {
  web3 = null;
}

export default web3;
