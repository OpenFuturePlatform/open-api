import { SET_CURRENT_ETH_ACCOUNT } from './types';
import web3 from '../utils/web3';
import eth from '../utils/eth';
import ethUnit from 'ethjs-unit';
import { openTokenSelector } from '../selectors/open-token';

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
  const openToken = openTokenSelector(getState());
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

export const unsubscribeEthAccount = () => () => clearInterval(ethAccountTimer);
