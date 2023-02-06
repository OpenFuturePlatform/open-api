const ERC20_ABI = [{ 'constant': true, 'inputs': [{ 'name': '_owner', 'type': 'address' }], 'name': 'balanceOf', 'outputs': [{ 'name': 'balance', 'type': 'uint256' }], 'payable': false, 'type': 'function' }];
const HOUSE_COIN_ADDRESS = "0xb8aDC4a353F9063524EeF9886171BE0a1823eb40";
const USDT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7"; // Ethereum
const TRON_ADDRESS = "0x85eac5ac2f758618dfa09bdbe0cf174e7d574d5b"; // BEP20

const STABLE_COIN_ADDRESS = {
    USDT_ADDRESS: "0xdAC17F958D2ee523a2206206994597C13D831ec7",
    TRON_ADDRESS: "0x85eac5ac2f758618dfa09bdbe0cf174e7d574d5b",
    HOUSE_COIN_ADDRESS: "0xb8aDC4a353F9063524EeF9886171BE0a1823eb40",
    USDC_ADDRESS: "0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48",
    BUSD_ADDRESS: "0x4Fabb145d64652a948d72533023f6E7A623C7C53"
};
module.exports = {STABLE_COIN_ADDRESS};