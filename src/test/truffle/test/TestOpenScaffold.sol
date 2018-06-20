pragma solidity ^0.4.0;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/OpenScaffold.sol";


contract TestOpenScaffold {

    OpenScaffold scaffold = OpenScaffold(DeployedAddresses.OpenScaffold());


    function testAddShareHolder() public {
        uint index = scaffold.addShareHolder(0x32539E7cd412335BeA8256e9f3dCf8288253326f, 30);

        uint expectedCount = 1;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Wrong!");

    }

    function testGetShareHolderAtIndex() public {
        uint expectedAddress = 0x0;

        Assert.equal(scaffold.getShareHolderAtIndex(0), 0x0, "Wrong!");
    }

    function testGetShareHolderCount() public {
        uint expectedCount = 0;

        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Wrong!");
    }

    function testGetHoldersShare() public {
        uint expectedCount = 0;

        Assert.equal(scaffold.getHoldersShare(0x69c4BB240cF05D51eeab6985Bab35527d04a8C64), expectedCount, "Wrong!");
    }

}
