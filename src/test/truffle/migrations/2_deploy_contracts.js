const OpenScaffold = artifacts.require("./OpenScaffold.sol");
const ExposedOpenScaffold = artifacts.require("./ExposedOpenScaffold.sol");

const developerAddress = web3.eth.accounts[0];
const platformAddress = web3.eth.accounts[1];

const fiatCurrency = "100";
const fiatAmount = "USD";
// amount in wei (1 ether = 1*10^18 wei)
const weiAmount = 10*10**18;

module.exports = function(deployer) {
  deployer.deploy(OpenScaffold, developerAddress, platformAddress, fiatCurrency, fiatAmount, weiAmount);
  deployer.link(OpenScaffold, ExposedOpenScaffold);
  deployer.deploy(ExposedOpenScaffold, developerAddress, platformAddress, fiatCurrency, fiatAmount, weiAmount);
};
