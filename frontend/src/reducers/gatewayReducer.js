import {FETCH_GATEWAY_APPLICATIONS} from "../actions/types";

export default function (state = {totalCount: 0, list: []}, action) {
    switch (action.type) {
        case FETCH_GATEWAY_APPLICATIONS:
            return action.payload;
        default:
            return state;
    }
}