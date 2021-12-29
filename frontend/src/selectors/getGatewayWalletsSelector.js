
import {getGatewayWalletSetSelector} from "./getGatewayWalletSet";

export const getGatewayWalletSelector = (state, gatewayId) => {
    const gatewaySet = getGatewayWalletSetSelector(state, gatewayId);
    return gatewaySet.wallets ||  { list: [] };
};
