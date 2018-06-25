var OpenScaffold = artifacts.require("./OpenScaffold.sol");
var ExposedOpenScaffold = artifacts.require("./ExposedOpenScaffold.sol");

module.exports = function(deployer) {
  deployer.deploy(OpenScaffold, 0x627306090abab3a6e1400e9345bc60c78a8bef57, 0xf17f52151ebef6c7334fad080c5704d77216b732, "description", "100", "usd", 10000);
  deployer.link(OpenScaffold, ExposedOpenScaffold);
  deployer.deploy(ExposedOpenScaffold, 0x627306090abab3a6e1400e9345bc60c78a8bef57, 0xf17f52151ebef6c7334fad080c5704d77216b732, "description", "100", "usd", 10000);
};
