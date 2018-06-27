const OpenScaffold = artifacts.require("./OpenScaffold");

contract('OpenScaffold JS TEST', (accounts) => {
    let metaContract;
    const vendorAddress = accounts[0];
    const platformAddress = accounts[1];
    const shareholderAddressOne = accounts[2];
    const shareholderAddressTwo = accounts[3];

    beforeEach('setup contract for each test', async () => {
        metaContract = await OpenScaffold.deployed();
    });
//
//     describe("Deploy contract", function() {
//         it("should have OpenScaffold deployed", () => {
//             assert(metaContract !== undefined, "OpenScaffold is deployed");
//         });
//     });
// //
//     describe("Testing constructor variables", function() {
//
//         it('has a vendor address', async () => {
//             assert.equal(await metaContract.vendorAddress.call(), vendorAddress);
//         });
//         it('has a developer address', async () => {
//             let scaffoldDescription = await metaContract.scaffoldDescription.call();
//             assert.equal(await !!scaffoldDescription, true, "Description is not set");
//             assert.equal(await scaffoldDescription, "test description", "Wrong description");
//         });
//         it('has a fiatAmount', async () => {
//             let fiatAmount = await metaContract.fiatAmount.call();
//             assert.equal(await !!fiatAmount, true, "FiatAmount is not set");
//             assert.equal(await fiatAmount, 100, "Wrong fiatAmount value");
//         });
//         it('has a fiatCurrency', async () => {
//             let fiatCurrency = await metaContract.fiatCurrency.call();
//             assert.equal(await !!fiatCurrency, true, "FiatCurrency is not set");
//             assert.equal(await fiatCurrency, "USD", "Wrong fiatCurrency value");
//         });
//         it('has a scaffoldAmount', async () => {
//             let scaffoldAmount  = await metaContract.scaffoldAmount.call();
//             let amountValue     = 10*10**18;
//             assert.equal(await !!scaffoldAmount, true, "ScaffoldAmount is not set");
//             assert.equal(await scaffoldAmount.toNumber(), amountValue, "Wrong scaffoldAmount value");
//         });
//
//     });
//
    describe("Testing Shareholder C.R.U.D. functional", async function() {

        // it('Create. Has an added shareholders', async () => {
        //     let shareOne = 20;
        //     let shareTwo = 30;
        //     await metaContract.addShareHolder(shareholderAddressOne, shareOne);
        //     await metaContract.addShareHolder(shareholderAddressTwo, shareTwo);
        //     let shareholdersCount = (await metaContract.getShareHolderCount()).toNumber();
        //     assert.equal(shareholdersCount, 2, "Wrong, shareholders not been added");
        // });
//         it('Read. Has an added shares and holders addresses', async () => {
//             let shareOne = 20;
//             let holdersTwoAddress   = await metaContract.getShareHolderAtIndex(1);
//             let holdersOneShare     = await metaContract.getHoldersShare(shareholderAddressOne);
//             assert.equal(holdersOneShare, shareOne, "Wrong, holders share is not equal set value");
//             assert.equal(holdersTwoAddress, shareholderAddressTwo, "Wrong, address share is not equal set value");
//         });
//         it('Update. Has an updateded shares', async () => {
//             let newUpdate;
//             let shareOne = 30;
//
//             let beforeUpdateHoldersOneShare = await metaContract.getHoldersShare(shareholderAddressOne);
//
//             await metaContract.editShareHolder(shareholderAddressOne, shareOne);
//
//             let holdersOneShare = await metaContract.getHoldersShare(shareholderAddressOne);
//
//             try {
//                 newUpdate = await metaContract.editShareHolder(accounts[4], shareOne);
//             } catch (e) {
//                 //console.log('exception: non-holder cannot get update');
//             }
//
//             assert.notEqual(beforeUpdateHoldersOneShare, holdersOneShare, "Wrong, holders share is not equal set value");
//             assert.equal(holdersOneShare, shareOne, "Wrong, holders share is not equal set value");
//             assert(newUpdate === undefined, 'non-holder can get update');
//         });
//         it('Delete. Has a delete one shareholder', async () => {
//             let beforeDeleteCount = (await metaContract.getShareHolderCount()).toNumber();
//
//             await metaContract.deleteShareHolder(shareholderAddressTwo);
//
//             let afterDeleteCount = (await metaContract.getShareHolderCount()).toNumber();
//
//             assert.notEqual(beforeDeleteCount, afterDeleteCount, "Wrong, shareholders not been deleted");
//         });
//
    });

    describe("Testing Helpers functions", async function() {

        it('Has a holders share', async () => {
            let share = 50;
            await metaContract.addShareHolder(accounts[4], share);
            let shareAmount = await metaContract.getHoldersShare(accounts[4]);

            assert.equal(shareAmount, share, "Wrong, share amount is not equal");
        });

        it('Has a share holder address and share from index', async () => {
            let shareHolderAtIndex = await metaContract.getShareHolderAtIndex(0);
            let holdersShare = await metaContract.getHoldersShare(shareHolderAtIndex);
            let addressAndShare = await metaContract.getShareHolderAddressAndShareAtIndex(0);
            let address = addressAndShare[0];
            let share   = addressAndShare[1].toNumber();

            assert.equal(address, shareHolderAtIndex, "Wrong, holders addresses is not equal");
            assert.equal(share, holdersShare, "Wrong, holders shares amount is not equal");
        });

        it('Has a shareholder address at index', async () => {
            let shareholdersThree = accounts[4];
            let shareHolderAtIndex = await metaContract.getShareHolderAtIndex(0);
            assert.isNotEmpty(shareHolderAtIndex, "Wrong, shareholder address is not exist");
            assert.equal(shareHolderAtIndex, shareholdersThree, "Wrong, shareholder address is not equal");
        });

        it('Has a shareholder count', async () => {
            let shareHolderCount = (await metaContract.getShareHolderCount()).toNumber();

            assert.notEqual(shareHolderCount, 0, "Wrong, shareholders count is zero");
        });


    });

    describe("Testing Main Scaffold functions", async function() {


        it('Has a new scaffold description', async () => {
            let oldDescription = await metaContract.scaffoldDescription.call();
             await metaContract.setDescription("new description");
            let newDescription = await metaContract.scaffoldDescription.call();
            assert.isNotEmpty(newDescription, "Wrong, description is empty");
            assert(newDescription !== oldDescription, "Wrong, shareholder description is not set");
        });

        it('Has a coins payed to shareholders', async () => {
//todo: test payed func
            console.log('Balance metaContract',web3.eth.getBalance(metaContract.address).toNumber());
        });


    })


});
