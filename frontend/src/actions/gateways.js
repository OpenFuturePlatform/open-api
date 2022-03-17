import {apiDelete, apiGet, apiPost} from "./apiRequest";
import {
    getGatewayApplicationsPath,
} from "../utils/apiPathes";
import {
    FETCH_GATEWAY_APPLICATIONS,
    SET_GATEWAY_APPLICATION_SET,
    SHOW_MODAL
} from "./types";
import {parseApiError} from "../utils/parseApiError";
import {getGatewayApplicationWallet} from "./gateway-wallet";


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

export const updateGatewayApplication = (id) => async (dispatch) => {
    const gateway = await dispatch(apiPost(getGatewayApplicationsPath(id)));
    const error = '';
    const payload = { id, gateway, error, loading: false };

    dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload });
};

export const fetchGatewayApplicationDetailsFromApi = (id) => async dispatch => {
    dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload: { id, loading: true } });

    try {
        const gateway = await dispatch(apiGet(getGatewayApplicationsPath(id)));

        const error = '';
        const payload = { id, gateway, error, loading: false };
        dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload });

    } catch (e) {
        const response = e.response || {};
        const error = `${response.status}: ${response.message || response.statusText}`;
        const payload = { id, error, loading: false };
        dispatch({ type: SET_GATEWAY_APPLICATION_SET, payload });
        throw e;
    }
};

export const fetchGatewayApplicationDetails = (id) => async dispatch => {
    dispatch(fetchGatewayApplicationDetailsFromApi(id));
    dispatch(getGatewayApplicationWallet(id));
};

export const removeGatewayApplication = (id) => async dispatch => {
    try {
        await dispatch(apiDelete(getGatewayApplicationsPath()+"?id="+id, {}));
        await dispatch(fetchGatewayApplications())
    } catch (e) {
        throw parseApiError(e);
    }
};

