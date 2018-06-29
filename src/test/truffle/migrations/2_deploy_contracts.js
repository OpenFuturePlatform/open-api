var OpenScaffold = artifacts.require("./OpenScaffold.sol");
var ExposedOpenScaffold = artifacts.require("./ExposedOpenScaffold.sol");

let vendorAddress = web3.eth.accounts[0];
let platformAddress = web3.eth.accounts[1];

let description = "test description";
let fiatCurrency = "100";
let fiatAmount = "USD";
// amount in wei (1 ether = 1*10^18 wei)
let weiAmount = 10*10**18;

module.exports = function(deployer) {
  deployer.deploy(OpenScaffold, vendorAddress, platformAddress, description, fiatCurrency, fiatAmount, weiAmount);
  deployer.link(OpenScaffold, ExposedOpenScaffold);
  deployer.deploy(ExposedOpenScaffold, vendorAddress, platformAddress, description, fiatCurrency, fiatAmount, weiAmount);
};
