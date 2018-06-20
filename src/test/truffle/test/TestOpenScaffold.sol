pragma solidity ^0.4.19;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/OpenScaffold.sol";


contract ExposedContract is OpenScaffold {

    function ExposedContract() public {}

    function _pay(address who, uint amount) public {
        pay(who, amount);
    }
}

contract TestOpenScaffold {

    address shareHolderAddress1 = 0x32539E7cd412335BeA8256e9f3dCf8288253326f;
    address shareHolderAddress2 = 0x62A64e70277B9DFf1EDE0E0032b5e4291df1599d;

    uint8 holderShare1 = 30;
    uint8 holderShare2 = 40;


    function createScaffold() public returns(OpenScaffold) {
        return new OpenScaffold(0x32539E7cd412335BeA8256e9f3dCf8288253326f,
                                0x32539E7cd412335BeA8256e9f3dCf8288253326f,
                                "description", "100", "usd", 10000);
    }


    function testAddShareHolder() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        uint expectedCount = 1;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Wrong!");
    }

    function testGetShareHolderAtIndex() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        address expectedAddress = shareHolderAddress1;
        Assert.equal(scaffold.getShareHolderAtIndex(0), expectedAddress, "Wrong!");
    }

    function testGetShareHolderCount() public {
        OpenScaffold scaffold = createScaffold();

        scaffold.addShareHolder(shareHolderAddress1, holderShare1);
        scaffold.addShareHolder(shareHolderAddress2, holderShare2);

        uint expectedCount = 2;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Wrong!");
    }

    function testGetHoldersShare() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        uint expectedShare = holderShare1;
        Assert.equal(scaffold.getHoldersShare(shareHolderAddress1), expectedShare, "Wrong!");
    }

    function testEditShareHolder() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        scaffold.editShareHolder(shareHolderAddress1, holderShare2);

        uint expectedShare = holderShare2;
        Assert.equal(scaffold.getHoldersShare(shareHolderAddress1), expectedShare, "Wrong!");
    }

    function testDeleteShareHolder() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        scaffold.deleteShareHolder(shareHolderAddress1);

        uint expectedCount = 0;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Wrong!");
    }

//    function testPay() public {
//        ExposedContract eScaffold = new ExposedContract();
//        OpenScaffold scaffold = createScaffold();
//        eScaffold._pay(0x32539E7cd412335BeA8256e9f3dCf8288253326f, 10000);
//    }

}
