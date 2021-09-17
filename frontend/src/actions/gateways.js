import {apiDelete, apiGet, apiPost} from "./apiRequest";
import {getEthereumShareHoldersPath, getGatewayApplicationsPath, getOpenScaffoldsPath} from "../utils/apiPathes";
import {DELETE_GATEWAY_APPLICATIONS, FETCH_GATEWAY_APPLICATIONS, SHOW_MODAL} from "./types";
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
    try {
        await dispatch(apiPost(getGatewayApplicationsPath(), formValues));
    } catch (e) {
        const error = parseApiError(e);
        dispatch({
            type: SHOW_MODAL,
            payload: {showLoader: false, showModal: true, error: error.message}
        });
        throw error;
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