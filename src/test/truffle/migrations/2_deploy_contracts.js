var OpenScaffold = artifacts.require("./OpenScaffold.sol");

module.exports = function(deployer) {
  deployer.deploy(OpenScaffold, 0x32539E7cd412335BeA8256e9f3dCf8288253326f, 0x32539E7cd412335BeA8256e9f3dCf8288253326f, "description", "100", "usd", 10000);
};
