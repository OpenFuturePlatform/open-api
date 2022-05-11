import {apiGet, apiPost} from "./apiRequest";
import {getUserTokenPath} from "../utils/apiPathes";
import {FETCH_USER_TOKENS, SHOW_MODAL} from "./types";
import {parseApiError} from "../utils/parseApiError";

export const fetchUserTokens = (offset = 0, limit = 10) => async dispatch => {
  const params = { offset, limit };

  try {
    const data = await dispatch(apiGet(getUserTokenPath(), params));
    dispatch({ type: FETCH_USER_TOKENS, payload: data });
  } catch (err) {
    const error = parseApiError(err);
    dispatch({
      type: SHOW_MODAL,
      payload: {showLoader: false, showModal: true, error: error.message}
    });
    throw error;
  }
};

export const saveUserToken = formValues => async (dispatch) => {
  await dispatch(apiPost(getUserTokenPath(), formValues));
};
