pragma solidity ^0.4.19;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/OpenScaffold.sol";


contract ExposedContract is OpenScaffold {

    function ExposedContract(
        address _vendorAddress,
        address _platformAddress,
        string _description,
        string _fiatAmount,
        string _fiatCurrency,
        uint _scaffoldAmount
        ) OpenScaffold(
            _vendorAddress,
            _platformAddress,
            _description,
            _fiatAmount,
            _fiatCurrency,
            _scaffoldAmount)
        public {
    }

    function _createScaffoldTransaction(address customerAddress) public returns(uint){
        createScaffoldTransaction(customerAddress);
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
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Shareholder wasn't added.");
    }

    function testGetShareHolderAtIndex() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        address expectedAddress = shareHolderAddress1;
        Assert.isNotZero(scaffold.getShareHolderAtIndex(0), "Shareholder address can not be empty.");
        Assert.equal(scaffold.getShareHolderAtIndex(0), expectedAddress, "Shareholder does not exist.");
    }

    function testGetShareHolderCount() public {
        OpenScaffold scaffold = createScaffold();

        scaffold.addShareHolder(shareHolderAddress1, holderShare1);
        scaffold.addShareHolder(shareHolderAddress2, holderShare2);

        uint expectedCount = 2;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Shareholders are not exist");
    }

    function testGetHoldersShare() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        uint expectedShare = holderShare1;
        Assert.equal(scaffold.getHoldersShare(shareHolderAddress1), expectedShare, "Shareholder doesn't have expected share amount");
    }

    function testEditShareHolder() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        scaffold.editShareHolder(shareHolderAddress1, holderShare2);

        uint expectedShare = holderShare2;
        Assert.equal(scaffold.getHoldersShare(shareHolderAddress1), expectedShare, "Edited shareholder doesn't have expected share amount");
    }

    function testDeleteShareHolder() public {
        OpenScaffold scaffold = createScaffold();
        scaffold.addShareHolder(shareHolderAddress1, holderShare1);

        scaffold.deleteShareHolder(shareHolderAddress1);

        uint expectedCount = 0;
        Assert.equal(scaffold.getShareHolderCount(), expectedCount, "Shareholder wasn't removed");
    }

    function testCreateScaffoldTransaction() public {
        ExposedContract eScaffold = new ExposedContract(0x32539E7cd412335BeA8256e9f3dCf8288253326f,
                                                        0x32539E7cd412335BeA8256e9f3dCf8288253326f,
                                                        "description", "100", "usd", 10000);

        eScaffold._createScaffoldTransaction(0x5aeda56215b167893e80b4fe645ba6d5bab767de);
    }

//    function testPay() public {
//        ExposedContract eScaffold = new ExposedContract(0x32539E7cd412335BeA8256e9f3dCf8288253326f,
//        0x32539E7cd412335BeA8256e9f3dCf8288253326f,
//        "description", "100", "usd", 10000);
//
//        eScaffold._pay(0x32539E7cd412335BeA8256e9f3dCf8288253326f, 10000);
//    }

}
