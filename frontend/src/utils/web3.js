import Web3 from 'web3';

const REACT_APP_INFURA_URL = process.env.REACT_APP_INFURA_URL;

let web3;

if (typeof window !== 'undefined' && typeof window.web3 !== 'undefined') {
  web3 = new Web3(window.web3.currentProvider);
} else {
  const provider = new Web3.providers.HttpProvider(REACT_APP_INFURA_URL);

  web3 = new Web3(provider);
}

export default web3;
