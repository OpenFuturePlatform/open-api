import {apiDelete, apiGet, apiPost} from "./apiRequest";
import {getGatewayApplicationWalletsPath} from "../utils/apiPathes";
import {FETCH_GATEWAY_APPLICATION_WALLET} from "./types";
import {parseApiError} from "../utils/parseApiError";

export const getGatewayApplicationWallet = (gatewayId) => async dispatch => {
  const wallets = await dispatch(apiGet(getGatewayApplicationWalletsPath(gatewayId), {}));
  dispatch({ type: FETCH_GATEWAY_APPLICATION_WALLET, payload: {gatewayId, wallets} });
};

export const generateGatewayApplicationWallet = (wallet, blockchain) => async (dispatch) => {
  await dispatch(apiPost(getGatewayApplicationWalletsPath(), {applicationId: wallet.applicationId, webHook: wallet.webHook, blockchainType: blockchain.blockchain}));
  dispatch(getGatewayApplicationWallet(wallet.applicationId));
};

export const removeGatewayApplicationWallet = (gatewayId, address) => async dispatch => {
  try {
    await dispatch(apiDelete( getGatewayApplicationWalletsPath()+"?applicationId="+gatewayId+"&address="+address, {}));
    await dispatch(getGatewayApplicationWallet(gatewayId));
  } catch (e) {
    throw parseApiError(e);
  }
};
