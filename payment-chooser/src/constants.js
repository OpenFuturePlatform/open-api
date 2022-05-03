const ERC20_ABI = [{ 'constant': true, 'inputs': [{ 'name': '_owner', 'type': 'address' }], 'name': 'balanceOf', 'outputs': [{ 'name': 'balance', 'type': 'uint256' }], 'payable': false, 'type': 'function' }];
const HOUSE_COIN_ADDRESS = "0x6Bf8526b51D4A1601Fed1046f13Dbf5aC663028E";
const USDT_ADDRESS = "0xdac17f958d2ee523a2206206994597c13d831ec7"; // Ethereum
const TRON_ADDRESS = "0x85eac5ac2f758618dfa09bdbe0cf174e7d574d5b"; // BEP20

const STABLE_COIN_ADDRESS = {
    USDT_ADDRESS: "0xdac17f958d2ee523a2206206994597c13d831ec7",
    TRON_ADDRESS: "0x85eac5ac2f758618dfa09bdbe0cf174e7d574d5b",
    HOUSE_COIN_ADDRESS: "0x6Bf8526b51D4A1601Fed1046f13Dbf5aC663028E"
};
module.exports = {STABLE_COIN_ADDRESS};