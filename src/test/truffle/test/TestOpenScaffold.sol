pragma solidity ^0.4.19;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/OpenScaffold.sol";
import "../contracts/ExposedOpenScaffold.sol";


contract TestOpenScaffold {

    address shareHolderAddress1 = 0xc5fdf4076b8f3a5357c5e395ab970b5b54098fef;
    address shareHolderAddress2 = 0x821aea9a577a9b44299b9c15c88cf3087f3b5544;

    uint8 holderShare1 = 30;
    uint8 holderShare2 = 40;


    function createScaffold() public returns(OpenScaffold) {
        return new OpenScaffold(0x627306090abab3a6e1400e9345bc60c78a8bef57,
                                0xf17f52151ebef6c7334fad080c5704d77216b732,
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
        ExposedOpenScaffold eScaffold = ExposedOpenScaffold(DeployedAddresses.ExposedOpenScaffold());

        uint index1 = eScaffold._createScaffoldTransaction(0x5aeda56215b167893e80b4fe645ba6d5bab767de);
        uint index2 = eScaffold._createScaffoldTransaction(0x5aeda56215b167893e80b4fe645ba6d5bab767de);

        uint expectedIndex = 2;
        Assert.equal(index2, expectedIndex, "Wrong index!");
    }

    function testPayToShareHoldersWithoutShareholders() public {
        ExposedOpenScaffold eScaffold = ExposedOpenScaffold(DeployedAddresses.ExposedOpenScaffold());

        uint expectedVendorAmount = 100;
        Assert.equal(eScaffold._payToShareHolders(100), expectedVendorAmount, "Wrong vendor amount");
    }

    function testPayToShareHoldersWithShareholders() public {
        ExposedOpenScaffold eScaffold = ExposedOpenScaffold(DeployedAddresses.ExposedOpenScaffold());
        eScaffold.addShareHolder(shareHolderAddress1, holderShare1);

        uint expectedVendorAmount = 100 - holderShare1;
        Assert.equal(eScaffold._payToShareHolders(100), expectedVendorAmount, "Wrong vendor amount");
    }

}
