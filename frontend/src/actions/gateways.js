import {apiDelete, apiGet, apiPost} from "./apiRequest";
import {
    getGatewayApplicationsPath,
    getGatewayApplicationWalletsPath,
} from "../utils/apiPathes";
import {
    FETCH_GATEWAY_APPLICATION_WALLET,
    FETCH_GATEWAY_APPLICATIONS, SET_ETHEREUM_SCAFFOLD_SET, SET_GATEWAY_APPLICATION_SET,
    SHOW_MODAL
} from "./types";
import {parseApiError} from "../utils/parseApiError";


export const fetchGatewayApplications = (offset = 0, limit = 10) => async dispatch => {
    const params = { offset, limit };

    try {
        const data = await dispatch(apiGet(getGatewayApplicationsPath(), params));
        dispatch({ type: FETCH_GATEWAY_APPLICATIONS, payload: data });
    } catch (err) {
        const error = parseApiError(err);
        dispatch({
            type: SHOW_MODAL,
            payload: {showLoader: false, showModal: true, error: error.message}
        });
        throw error;
    }
};

export const saveGatewayApplication = formValues => async (dispatch) => {
    await dispatch(apiPost(getGatewayApplicationsPath(), formValues));
};

export const fetchGatewayApplicationDetails = (id) => async dispatch => {
    dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload: { id, loading: true } });

    try {
        const gateway = await dispatch(apiGet(getGatewayApplicationsPath(id)));
        const wallets = await dispatch(getGatewayApplicationWallet(id));
        const error = '';
        const payload = { id, gateway, wallets, error, loading: false };
        dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload });

    } catch (e) {
        const response = e.response || {};
        const error = `${response.status}: ${response.message || response.statusText}`;
        const payload = { id, error, loading: false };
        dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload });
        throw e;
    }
};

export const removeGatewayApplication = (id) => async dispatch => {
    try {
        await dispatch(apiDelete(getGatewayApplicationsPath()+"?id="+id, {}));
        await dispatch(fetchGatewayApplications())
    } catch (e) {
        throw parseApiError(e);
    }
};

export const getGatewayApplicationWallet = (gatewayId) => async dispatch => {
    const wallets = await dispatch(apiGet(getGatewayApplicationWalletsPath(gatewayId), {}));
    const error = '';
    dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload: {gatewayId, wallets, error, loading: false} });
    return wallets;
};

export const generateGatewayApplicationWallet = (wallet, blockchain) => async (dispatch) => {
    await dispatch(apiPost(getGatewayApplicationWalletsPath(), {applicationId: wallet.applicationId, webHook: wallet.webHook, blockchainType: blockchain.blockchain}));
    dispatch(fetchGatewayApplicationDetails(wallet.applicationId))
};

export const removeGatewayApplicationWallet = (gatewayId, address) => async dispatch => {
    try {
        await dispatch(apiDelete( getGatewayApplicationWalletsPath()+"?applicationId="+gatewayId+"&address="+address, {}));
        await dispatch(fetchGatewayApplicationDetails(gatewayId))
    } catch (e) {
        throw parseApiError(e);
    }
};