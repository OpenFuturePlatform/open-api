import {SET_CURRENT_ETH_ACCOUNT} from './types';
import web3 from '../utils/web3';
import eth from '../utils/eth';
import ethUnit from 'ethjs-unit';
import {openTokenSelectorEth} from '../selectors/open-token';
import {ERC20_ABI} from "../const";

const setNetworkId = () => async dispatch => {
  const activeNetworkId = await web3.eth.net.getId();
  dispatch({
    type: SET_CURRENT_ETH_ACCOUNT,
    payload: { activeNetworkId }
  });
};

const setEthAccount = account => async (dispatch, getState) => {
  if (!account) {
    dispatch({
      type: SET_CURRENT_ETH_ACCOUNT,
      payload: { account: '', balance: null }
    });
    return;
  }

  const ethBalanceResult = await eth.getBalance(account);
  const ethBalance = ethUnit.fromWei(ethBalanceResult, 'ether');
  const openToken = openTokenSelectorEth(getState());
  const tokenBalanceResults = await openToken.balanceOf(account);
  const supply = Number(tokenBalanceResults[0]) / 100000000;
  const tokenBalance = supply.toString();
  dispatch({
    type: SET_CURRENT_ETH_ACCOUNT,
    payload: { account, ethBalance, tokenBalance }
  });
};

let ethAccountTimer;

export const subscribeEthAccount = () => async dispatch => {
  if (ethAccountTimer || !web3) {
    return;
  }
  let account;
  ethAccountTimer = setInterval(() => {
    dispatch(setNetworkId());
    web3.eth.getAccounts((error, newAccounts) => {
      if (newAccounts[0] !== account) {
        account = newAccounts[0];
        dispatch(setEthAccount(account));
      }
    });
  }, 1000);
  return dispatch(setNetworkId());
};

export const getErc20Token = address => async dispatch => {
  if (!web3 || address === undefined) {
    return;
  }

  const tokenContract = new web3.eth.Contract(ERC20_ABI, address);
  const symbol = await tokenContract.methods.symbol().call();
  const decimals = await tokenContract.methods.decimals().call();
  const name = await tokenContract.methods.name().call();

  return {address: address, name: name, symbol: symbol, decimal: decimals};
};

export const unsubscribeEthAccount = () => () => {
  clearInterval(ethAccountTimer);
  ethAccountTimer = null;
};
