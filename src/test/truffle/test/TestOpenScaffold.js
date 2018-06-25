const OpenScaffold = artifacts.require("./OpenScaffold");

contract('OpenScaffold', (accounts) => {
    let metaContract;
    const vendorAddress = accounts[0];
    const platformAddress = accounts[1];
    const shareholderAddressOne = accounts[2];
    const shareholderAddressTwo = accounts[3];

    beforeEach('setup contract for each test', async () => {
        metaContract = await OpenScaffold.deployed();
    });
//
    describe("Deploy contract", function() {
        it("should have OpenScaffold deployed", () => {
            assert(metaContract !== undefined, "OpenScaffold is deployed");
        });
    });
//
    describe("Testing constructor variables", function() {

        it('has an vendor address', async () => {
            assert.equal(await metaContract.vendorAddress.call(), vendorAddress);
        });
        it('has an developer address', async () => {
            let scaffoldDescription = await metaContract.scaffoldDescription.call();
            assert.equal(await !!scaffoldDescription, true, "Description is not set");
            assert.equal(await scaffoldDescription, "description", "Wrong description");
        });
        it('has an fiatAmount', async () => {
            let fiatAmount = await metaContract.fiatAmount.call();
            assert.equal(await !!fiatAmount, true, "FiatAmount is not set");
            assert.equal(await fiatAmount, 100, "Wrong fiatAmount value");
        });
        it('has an fiatCurrency', async () => {
            let fiatCurrency = await metaContract.fiatCurrency.call();
            assert.equal(await !!fiatCurrency, true, "FiatCurrency is not set");
            assert.equal(await fiatCurrency, "usd", "Wrong fiatCurrency value");
        });
        it('has an scaffoldAmount', async () => {
            let scaffoldAmount  = await metaContract.scaffoldAmount.call();
            let amountValue     = 10*10**18;
            assert.equal(await !!scaffoldAmount, true, "ScaffoldAmount is not set");
            assert.equal(await scaffoldAmount.toNumber(), amountValue, "Wrong scaffoldAmount value");
        });

    });

    describe("Testing Shareholder C.R.U.D. functional", async function() {

        it('Create. Has an added shareholders', async () => {
            let shareOne = 20;
            let shareTwo = 30;
            await metaContract.addShareHolder(shareholderAddressOne, shareOne);
            await metaContract.addShareHolder(shareholderAddressTwo, shareTwo);
            let shareholdersCount = (await metaContract.getShareHolderCount()).toNumber();
            assert.equal(shareholdersCount, 2, "Wrong, shareholders not been added");
        });
        it('Read. Has an added shares and holders addresses', async () => {
            let shareOne = 20;
            let holdersTwoAddress   = await metaContract.getShareHolderAtIndex(1);
            let holdersOneShare     = await metaContract.getHoldersShare(shareholderAddressOne);
            assert.equal(holdersOneShare, shareOne, "Wrong, holders share is not equal set value");
            assert.equal(holdersTwoAddress, shareholderAddressTwo, "Wrong, address share is not equal set value");
        });
        it('Update. Has an updateded shares', async () => {
            let newUpdate;
            let shareOne = 30;

            let beforeUpdateHoldersOneShare = await metaContract.getHoldersShare(shareholderAddressOne);

            await metaContract.editShareHolder(shareholderAddressOne, shareOne);

            let holdersOneShare = await metaContract.getHoldersShare(shareholderAddressOne);

            try {
                newUpdate = await metaContract.editShareHolder(accounts[4], shareOne);
            } catch (e) {
                //console.log('exception: non-holder cannot get update');
            }

            assert.notEqual(beforeUpdateHoldersOneShare, holdersOneShare, "Wrong, holders share is not equal set value");
            assert.equal(holdersOneShare, shareOne, "Wrong, holders share is not equal set value");
            assert(newUpdate === undefined, 'non-holder can get update');
        });
        it('Delete. Has an delete one shareholder', async () => {
            let beforeDeleteCount = (await metaContract.getShareHolderCount()).toNumber();

            await metaContract.deleteShareHolder(shareholderAddressTwo);

            let afterDeleteCount = (await metaContract.getShareHolderCount()).toNumber();

            assert.notEqual(beforeDeleteCount, afterDeleteCount, "Wrong, shareholders not been deleted");
        });

    });

    describe("Testing Helpers functions", async function() {

        it('Has an get share holder address and share from index', async () => {
            let shareHolderAtIndex = await metaContract.getShareHolderAtIndex(0);
            let holdersShare = await metaContract.getHoldersShare(shareHolderAtIndex);
            let addressAndShare = await metaContract.getShareHolderAddressAndShareAtIndex(0);

            assert.equal(addressAndShare[0], shareHolderAtIndex, "Wrong, holders addresses is not equal");
            assert.equal(addressAndShare[1].toNumber(), holdersShare, "Wrong, holders shares amount is not equal");
        });
        //getHoldersShare
        it('Has an holders share', async () => {
            let shareAmount = await metaContract.partners[shareholderAddressOne].share;
            console.log('shareAmount',shareAmount);
            // assert.equal(addressAndShare[0], shareHolderAtIndex, "Wrong, holders addresses is not equal");
            // assert.equal(addressAndShare[1].toNumber(), holdersShare, "Wrong, holders shares amount is not equal");
        });

    })


});
