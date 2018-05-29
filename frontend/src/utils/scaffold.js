import web3 from './web3';

export default (abiData, address) => {
  return new web3.eth.Contract(JSON.parse(abiData), address);
};
