import axios from 'axios';
import eth from '../utils/eth';
import openToken from '../utils/open-token';
import {SET_CURRENT_ETH_ACCOUNT} from './types';
import {fetchScaffoldItemFromApi} from './scaffolds';

export const activateScaffold = (scaffoldAddress, fromAddress, amount = '10.0') => async dispatch => {
  const hash = await openToken.transfer(scaffoldAddress, amount * 100000000, {from: fromAddress});

  dispatch({type: SET_CURRENT_ETH_ACCOUNT, payload: {activating: true, activatingHash: hash}});
  dispatch(subscribeScaffoldActivation(hash, scaffoldAddress));
};

export const deactivateScaffold = (scaffoldAddress, abi, developerAddress) => async dispatch => {
  const scaffoldContract = eth.contract(JSON.parse(abi)).at(scaffoldAddress);
  const hash = await scaffoldContract.deactivate({from: developerAddress});

  dispatch({type: SET_CURRENT_ETH_ACCOUNT, payload: {activating: true, activatingHash: hash}});
  dispatch(subscribeScaffoldActivation(hash, scaffoldAddress));
};

export const deactivateScaffoldByApi = (scaffoldAddress) => async dispatch => {
  try {
    await axios.post(`/api/scaffolds/${scaffoldAddress}/doDeactivate`);
    fetchScaffoldItemFromApi(scaffoldAddress)(dispatch);
  } catch (err) {
    console.log('Error deactivating scaffolds', err);
  }
};

let interval;

export const subscribeScaffoldActivation = (hash, scaffoldAddress) => async dispatch => {
  if (!hash) {
    dispatch({type: SET_CURRENT_ETH_ACCOUNT, payload: {activating: false, activatingHash: null}});
  }

  if (interval || !hash) {
    return;
  }

  interval = setInterval(async () => {
    try {
      const receipt = await eth.getTransactionReceipt(hash);
      if (!receipt) return;
      clearInterval(interval);
      interval = null;
      dispatch({type: SET_CURRENT_ETH_ACCOUNT, payload: {activating: false, activatingHash: null}});
      dispatch(fetchScaffoldItemFromApi(scaffoldAddress));
    } catch (reason) {
      clearInterval(interval);
    }
  }, 1000);
};
