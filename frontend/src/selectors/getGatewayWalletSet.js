export const getGatewayWalletSetSelector = ({ gatewayById }, gatewayId) => {
    return gatewayById[gatewayId] || {};
};