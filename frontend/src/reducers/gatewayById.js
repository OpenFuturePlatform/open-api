import {
  FETCH_GATEWAY_APPLICATION_WALLET,
  SET_GATEWAY_APPLICATION_SET,
} from "../actions/types";

const gatewayWalletReducer = (state = {}, action) => {
  switch (action.type) {
    case FETCH_GATEWAY_APPLICATION_WALLET:
      const list  = action.payload.wallets;
      return { ...state, list};
    default:
      return state;
  }
}

export default function(state = {}, action) {
    switch (action.type) {
        case SET_GATEWAY_APPLICATION_SET: {
            const gatewayId = action.payload.id;
            const oldGatewaySet = state[gatewayId] || {};
            const gatewaySet = action.payload;
            return { ...state, [gatewayId]: { ...oldGatewaySet, ...gatewaySet } };
        }
        case FETCH_GATEWAY_APPLICATION_WALLET: {
          const gatewayId = action.payload.gatewayId;
          const oldGatewaySet = state[gatewayId] || {};
          const wallets = gatewayWalletReducer(oldGatewaySet.wallets, action);
          return { ...state, [gatewayId]: { ...oldGatewaySet, wallets } };
        }
        default:
            return state;
    }
}
