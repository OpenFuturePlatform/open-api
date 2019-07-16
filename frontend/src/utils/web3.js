import Web3 from 'web3';

let web3;

if (typeof window !== 'undefined' && typeof window.web3 !== 'undefined' && window.web3.currentProvider.isMetaMask) {
  window.ethereum && window.ethereum.enable()
  web3 = new Web3(window.web3.currentProvider);
} else {
  web3 = null;
}

export const getWeb3Contract = ({ abi, address, parsedAbi }) =>
  web3 ? new web3.eth.Contract(parsedAbi || JSON.parse(abi), address) : null;

export default web3;
