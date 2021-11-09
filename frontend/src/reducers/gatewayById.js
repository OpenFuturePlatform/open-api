import {
    SET_GATEWAY_APPLICATION_SET,
} from "../actions/types";

export default function(state = {}, action) {
    switch (action.type) {
        case SET_GATEWAY_APPLICATION_SET: {
            const gatewayId = action.payload.id;
            const oldGatewaySet = state[gatewayId] || {};
            const gatewaySet = action.payload;
            return { ...state, [gatewayId]: { ...oldGatewaySet, ...gatewaySet } };
        }
        default:
            return state;
    }
}