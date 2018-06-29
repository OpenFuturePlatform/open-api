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

    describe("Deploy contract", function() {
        it("should have OpenScaffold deployed", () => {
            assert(metaContract !== undefined, "OpenScaffold is deployed");
        });
    });
//
    describe("Testing constructor variables", function() {

        it('has a vendor address', async () => {
            assert.equal(await metaContract.vendorAddress.call(), vendorAddress);
        });
        it('has a developer address', async () => {
            let scaffoldDescription = await metaContract.scaffoldDescription.call();
            assert.equal(await !!scaffoldDescription, true, "Description is not set");
            assert.equal(await scaffoldDescription, "test description", "Wrong description");
        });
        it('has a fiatAmount', async () => {
            let fiatAmount = await metaContract.fiatAmount.call();
            assert.equal(await !!fiatAmount, true, "FiatAmount is not set");
            assert.equal(await fiatAmount, 100, "Wrong fiatAmount value");
        });
        it('has a fiatCurrency', async () => {
            let fiatCurrency = await metaContract.fiatCurrency.call();
            assert.equal(await !!fiatCurrency, true, "FiatCurrency is not set");
            assert.equal(await fiatCurrency, "USD", "Wrong fiatCurrency value");
        });
        it('has a scaffoldAmount', async () => {
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
        it('Delete. Has a delete one shareholder', async () => {
            let beforeDeleteCount = (await metaContract.getShareHolderCount()).toNumber();

            await metaContract.deleteShareHolder(shareholderAddressTwo);

            let afterDeleteCount = (await metaContract.getShareHolderCount()).toNumber();

            assert.notEqual(beforeDeleteCount, afterDeleteCount, "Wrong, shareholders not been deleted");
        });

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
            let shareHolderAtIndex = await metaContract.getShareHolderAtIndex(0);

            assert.isNotEmpty(shareHolderAtIndex, "Wrong, shareholder address is not exist");
            assert.equal(shareHolderAtIndex, shareholderAddressOne, "Wrong, shareholder address is not equal");
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
            // customer wallet
            let accountForPay;
            // amount of coins sent
            let coinsForSend = 10;
            // balance before send coins
            let vendorAddressBalanceBeforeSend;
            let platformAddressBalanceBeforeSend;
            let shrHldrAddrOneBalanceBeforeSend;
            let shrHldrAddrTwoBalanceBeforeSend;
            // balance after send coins
            let vendorAddressBalanceAfterSend;
            let platformAddressBalanceAfterSend;
            let shrHldrAddrOneBalanceAfterSend;
            let shrHldrAddrTwoBalanceAfterSend;
            // get amount coins tranfered to recipient
            let vendorAmountTransferedCoins;
            let platformAmountTransferedCoins;
            let shrHldrAddrOneAmountTransferedCoins;
            let shrHldrAddrTwoAmountTransferedCoins;


            function getBalance(account) {
                return (web3.fromWei(web3.eth.getBalance(account))).toNumber();
            }


            // get a wallet with coins
            for (let i of[7, 8, 9]) {
                let balance = getBalance(accounts[i]);

                if(balance >= 10){
                    accountForPay = accounts[i];
                    break;
                }
            }



            // get accounts balance before send coins
            vendorAddressBalanceBeforeSend      = await (getBalance(accounts[0]));
            platformAddressBalanceBeforeSend    = await (getBalance(accounts[1]));
            shrHldrAddrOneBalanceBeforeSend     = await (getBalance(accounts[2]));
            shrHldrAddrTwoBalanceBeforeSend     = await (getBalance(accounts[4]));

            // get share for shareholder
            let hldrOneShare = (await metaContract.getHoldersShare(accounts[2])).toNumber();
            let hldrTwoShare = (await metaContract.getHoldersShare(accounts[4])).toNumber();
            let vendorShare  =  100 - (hldrOneShare + hldrTwoShare + 3);

             new Promise((resolve, reject) => {

                // pay from customer to contract
                var handleReceipt = (error, receipt) => {
                    if (error) reject(error);
                    else {
                        console.log(receipt);
                        resolve(receipt)
                    }
                }

                return web3.eth.sendTransaction({
                    from: accountForPay,
                    to: metaContract.address,
                    value: web3.toWei(coinsForSend.toString(), "ether"),
                    gas: 200000,

                }, handleReceipt)

            }).then((transaction) => {
                // get accounts balance after send coins
                vendorAddressBalanceAfterSend   =  getBalance(accounts[0]);
                platformAddressBalanceAfterSend =  getBalance(accounts[1]);
                shrHldrAddrOneBalanceAfterSend  =  getBalance(accounts[2]);
                shrHldrAddrTwoBalanceAfterSend  =  getBalance(accounts[4]);

                // get amount coins transfered to recipient
                vendorAmountTransferedCoins     = vendorAddressBalanceAfterSend - vendorAddressBalanceBeforeSend;
                platformAmountTransferedCoins   = platformAddressBalanceAfterSend - platformAddressBalanceBeforeSend;
                shrHldrAddrOneAmountTransferedCoins = shrHldrAddrOneBalanceAfterSend - shrHldrAddrOneBalanceBeforeSend;
                shrHldrAddrTwoAmountTransferedCoins = shrHldrAddrTwoBalanceAfterSend - shrHldrAddrTwoBalanceBeforeSend;


                 let summAllSentCoinsFromAccounts = Math.round(vendorAmountTransferedCoins +
                                                             platformAmountTransferedCoins +
                                                             shrHldrAddrOneAmountTransferedCoins +
                                                             shrHldrAddrTwoAmountTransferedCoins);

                 // pay for everyone
                 let payForPlatform = coinsForSend*3/100;
                 let payForShrHldrOne = (coinsForSend - payForPlatform)*hldrOneShare/100;
                 let payForShrHldrTwo = (coinsForSend - payForPlatform)*hldrTwoShare/100;
                 let payForVendor = coinsForSend - (payForPlatform + payForShrHldrOne + payForShrHldrTwo)



                assert.equal( summAllSentCoinsFromAccounts,
                    coinsForSend,
                    "Wrong, invalid value of the amount of all coins sent to accounts");

                assert.equal( platformAddressBalanceAfterSend.toFixed(2),
                    (platformAddressBalanceBeforeSend + payForPlatform).toFixed(2),
                    "Wrong, incorrect value of the amount in percentage terms");

                assert.equal( vendorAddressBalanceAfterSend.toFixed(2),
                    (vendorAddressBalanceBeforeSend + payForVendor).toFixed(2),
                    "Wrong, incorrect value of the amount in percentage terms");

                assert.equal( shrHldrAddrOneBalanceAfterSend.toFixed(2),
                    (shrHldrAddrOneBalanceBeforeSend + payForShrHldrOne).toFixed(2),
                    "Wrong, incorrect value of the amount in percentage terms");

                assert.equal( shrHldrAddrTwoBalanceAfterSend.toFixed(2),
                    (shrHldrAddrTwoBalanceBeforeSend + payForShrHldrTwo).toFixed(2),
                    "Wrong, incorrect value of the amount in percentage terms");

            })
        });


    })


});
