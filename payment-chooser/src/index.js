import * as style from './style.css';
import Web3 from "web3";
import Swal from "sweetalert2";
const contracts = require('eth-contract-metadata')

let widgetData;

const spinner = document.getElementById("spinner");
const orderKey = document.body.dataset.order;
const host = document.body.dataset.host;
const currency = document.body.dataset.currency;
const OPEN_URL = `${host}/widget/payment/addresses/order/${orderKey}`;
let timerId = 1;
let currentAccount = null;
let web3;
const amountSelector = document.querySelector(".amount");
const remainingSelector = document.querySelector(".remaining");
let leftAmount;

const ERC20_ABI = require("../src/contract-abi.json") ;
const {STABLE_COIN_ADDRESS} = require("../src/constants.js");

let paymentAddress;
let paymentRate;
let paymentNetwork;

async function getAddressData(URI) {
    let response = await fetch(URI, {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8;'
        })
    });

    return await response.json();
}

function fetchTransactionData() {
    spinner.setAttribute('hidden', '');
    $.ajax({
        url: OPEN_URL,
        type: 'get',
        success: function (data) {
            setTimeout(function () {
                spinner.removeAttribute('hidden');

            }, 5000);
            loadTransactionData(data);
        },
        complete: function (data) {
            setTimeout(fetchTransactionData, 5000);
        }
    });
}

function loadTransactionData(result) {

    amountSelector.innerHTML = `${result.orderAmount}` + ` ${currency}`;
    leftAmount = result.orderAmount - result.paid;
    remainingSelector.innerHTML = `${leftAmount}` + ` ${currency}`;

    if (result.orderAmount <= result.paid) {
        clearInterval(timerId);
        const counter = document.getElementById("countdown");
        counter.setAttribute('class', 'completed')
        counter.innerHTML = "Order Completed";
    }

    for (let blockchain of result.wallets) {
        const tbody = document.querySelector("." + `${blockchain.blockchain + blockchain.address}`);
        tbody.innerHTML = '';
        for (let trx of blockchain.transactions) {
            let rowBody = document.createElement('tr');
            let td_1 = document.createElement('td');
            td_1.innerHTML = `${trx.hash}`;
            let td_2 = document.createElement('td');
            td_2.innerHTML = `${trx.from}`;
            let td_3 = document.createElement('td');
            td_3.innerHTML = `${trx.amount}`;
            let td_4 = document.createElement('td');
            if (blockchain.native)
                td_4.innerHTML = `${trx.rate}` + ` ${currency}`;
            else
                td_4.innerHTML =  `${trx.token}`;
            rowBody.appendChild(td_1);
            rowBody.appendChild(td_2);
            rowBody.appendChild(td_3);
            rowBody.appendChild(td_4);
            tbody.appendChild(rowBody);
        }
    }
}

function setTransactions(blockchain, cardDivBody, cardDiv, card, accordion) {
    ////// Transactions /////
    let cardDivBodyAddressTrx = document.createElement('div');
    cardDivBodyAddressTrx.setAttribute('class', 'table-responsive mb-2 mb-md-0');
    let cardDivBodyAddressTrxTable = document.createElement('table');
    cardDivBodyAddressTrxTable.setAttribute('class', 'table table-hover');
    let cardDivBodyAddressTrxTableHead = document.createElement('thead');
    cardDivBodyAddressTrxTableHead.setAttribute('class', 'thead-light');
    let row = document.createElement('tr');
    let heading_1 = document.createElement('th');
    heading_1.innerHTML = "Txn Hash";
    let heading_2 = document.createElement('th');
    heading_2.innerHTML = "From";
    let heading_3 = document.createElement('th');
    heading_3.innerHTML = "Value";
    let heading_4 = document.createElement('th');
    heading_4.innerHTML = "Rate";
    row.appendChild(heading_1);
    row.appendChild(heading_2);
    row.appendChild(heading_3);
    row.appendChild(heading_4);
    cardDivBodyAddressTrxTableHead.appendChild(row);
    let cardDivBodyAddressTrxTableBody = document.createElement('tbody');
    cardDivBodyAddressTrxTableBody.setAttribute('class', `${blockchain.blockchain + blockchain.address}`);
    cardDivBodyAddressTrxTable.appendChild(cardDivBodyAddressTrxTableHead);
    cardDivBodyAddressTrxTable.appendChild(cardDivBodyAddressTrxTableBody);
    cardDivBodyAddressTrx.appendChild(cardDivBodyAddressTrxTable);
    cardDivBody.appendChild(cardDivBodyAddressTrx);
    ////////////////////////////////////////////////////
    cardDiv.appendChild(cardDivBody);
    card.appendChild(cardDiv);

    accordion.appendChild(card);
}

async function openPaymentWidget() {

    widgetData = await getAddressData(OPEN_URL);

    amountSelector.innerHTML = `${widgetData.orderAmount}` + ` ${currency}`;
    leftAmount = widgetData.orderAmount - widgetData.paid;
    remainingSelector.innerHTML = `${leftAmount}` + ` ${currency}`;

    countdownTimer(new Date(`${widgetData.orderDate}`).getTime(), widgetData.orderAmount, widgetData.paid);

    const accordion = document.querySelector(".accordion");
    let i = 1;
    for (let blockchain of widgetData.wallets) {
        let card = document.createElement('div');
        card.setAttribute('class', 'card');

        let cardHeader = document.createElement('div');
        cardHeader.setAttribute('class', 'card-header');
        cardHeader.setAttribute('id', 'headingOne');

        let cardHeaderH5 = document.createElement('h5');
        cardHeaderH5.setAttribute('class', 'mb-0');
        let cardHeaderH5Button = document.createElement('button');
        cardHeaderH5Button.setAttribute('class', 'btn btn-link');
        cardHeaderH5Button.setAttribute('type', 'button');
        cardHeaderH5Button.setAttribute('data-toggle', 'collapse');
        cardHeaderH5Button.setAttribute('data-target', '#collapse' + i + 'b');
        cardHeaderH5Button.setAttribute('aria-expanded', 'true');
        cardHeaderH5Button.setAttribute('aria-controls', 'collapse' + i + 'b');

        let blockChain = getBlockchainObject(blockchain);
        let cardHeaderH5img = document.createElement('img');
        cardHeaderH5img.setAttribute('class', 'align-middle blockchain-icon');
        cardHeaderH5img.setAttribute('src', blockChain['imgSrc']);

        cardHeaderH5Button.innerHTML = blockChain['title'];
        cardHeaderH5Button.appendChild(cardHeaderH5img);
        cardHeaderH5.appendChild(cardHeaderH5Button);
        cardHeader.appendChild(cardHeaderH5);
        card.appendChild(cardHeader);

        // Card Body Block
        let cardDiv = document.createElement('div');
        cardDiv.setAttribute('id', 'collapse' + i + 'b');
        cardDiv.setAttribute('class', 'collapse');
        cardDiv.setAttribute('data-parent', '#accordionExample');
        let cardDivBody = document.createElement('div');
        cardDivBody.setAttribute('class', 'card-body');
        cardDivBody.setAttribute('id', `${blockchain.address}`);

        // QR Code Block
        let cardDivBodyQrCode = document.createElement('div');
        let qrCodeId = 'qrcode' + i + 'b';
        cardDivBodyQrCode.setAttribute('id', qrCodeId);
        cardDivBody.appendChild(cardDivBodyQrCode);

        // Pay Metamask Block
        let cardDivBodyAddressModal = document.createElement('button');
        cardDivBodyAddressModal.setAttribute("data-rate", blockchain.rate);
        cardDivBodyAddressModal.setAttribute("data-address", blockchain.address);
        cardDivBodyAddressModal.setAttribute("data-chain", blockChain['chainID']);
        cardDivBodyAddressModal.setAttribute("class", "btn btn-primary pay-now");
        cardDivBodyAddressModal.innerHTML = "Pay with Metamask";
        cardDivBody.appendChild(cardDivBodyAddressModal);

        // Address Block
        let cardDivBodyAddress = document.createElement('div');
        cardDivBodyAddress.setAttribute('class', 'address');
        let cardDivBodyAddressHref = document.createElement('a');
        cardDivBodyAddressHref.setAttribute('href', blockChain['explorerUrl']);
        cardDivBodyAddressHref.setAttribute('target', '_blank');
        cardDivBodyAddressHref.innerHTML = blockchain.address;
        cardDivBodyAddress.appendChild(cardDivBodyAddressHref);

        cardDivBody.appendChild(cardDivBodyAddress);
        setTransactions(blockchain, cardDivBody, cardDiv, card, accordion);

        let generatedQrCode = new QRCode(qrCodeId, {
            text: blockchain.address,
            width: 128,
            height: 128,
            colorDark: "#000000",
            colorLight: "#ffffff",
            correctLevel: QRCode.CorrectLevel.H
        });

        i++;

    }

    connectMetamask()
    fetchTransactionData()
}

function getBlockchainObject(blockchain) {
    let IDs = {};
    if (blockchain.blockchain === "BitcoinBlockchain") {
        IDs['imgSrc'] = "/static/images/BTC.png";
        IDs['title'] = "Bitcoin";
        IDs['explorerUrl'] = "https://www.blockchain.com/btc/address/" + blockchain.address;
        IDs['chainID'] = "";
    } else if (blockchain.blockchain === "EthereumBlockchain") {
        IDs['imgSrc'] = "/static/images/ETH.png";
        IDs['title'] = "Ethereum";
        IDs['explorerUrl'] = "https://etherscan.io/address/" + blockchain.address;
        IDs['chainID'] = "1";
    } else if (blockchain.blockchain === "RopstenBlockchain") {
        IDs['imgSrc'] = "/static/images/ETH.png";
        IDs['title'] = "Ropsten";
        IDs['explorerUrl'] = "https://ropsten.etherscan.io/address/" + blockchain.address;
        IDs['chainID'] = "3";
    } else if (blockchain.blockchain === "BinanceTestnetBlockchain") {
        IDs['imgSrc'] = "/static/images/BNB.png";
        IDs['title'] = "Binance Test";
        IDs['explorerUrl'] = "https://testnet.bscscan.com/address/" + blockchain.address;
        IDs['chainID'] = "97";
    } else {
        IDs['imgSrc'] = "/static/images/BNB.png";
        IDs['title'] = "Binance";
        IDs['explorerUrl'] = "https://bscscan.com/address/" + blockchain.address;
        IDs['chainID'] = "56";
    }

    return IDs;
}

function countdownTimer(countDownDate, amount, paid) {

    timerId = setInterval(function () {

        let now = new Date().getTime();
        let distance = countDownDate - now;

        let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        let seconds = Math.floor((distance % (1000 * 60)) / 1000);

        const counter = document.getElementById("countdown");
        counter.innerHTML = hours + "h:" + minutes + "m:" + seconds + "s";

        if (distance < 0) {
            clearInterval(timerId);
            counter.setAttribute('class', 'expired')
            counter.innerHTML = "EXPIRED";
        }
        if (amount <= paid) {
            clearInterval(timerId);
            counter.setAttribute('class', 'completed')
            counter.innerHTML = "Order Completed";
        }
    }, 1000);
}

function calculateCryptoAmount(e) {
    let amount = $('#cryptoAmount').val();
    let rate = $('#rate').val();
    let finalAmount = parseFloat(rate) * parseFloat(amount);

    console.log(leftAmount);
    /*if (finalAmount > leftAmount){
        e.preventDefault();
        Swal.fire('Overdraft');
    }*/
    $('#cryptoTotal').val(parseFloat(finalAmount).toFixed(8));
}

function connectMetamask() {

    let m = detectMetaMask();

    if (m) {
        $('#metaicon').removeClass('meta-gray')
        $('#metaicon').addClass('meta-normal')
        $('#enableMetamask').attr('disabled', false)
        web3 = new Web3(window.ethereum);
        connectAccount() // Make sure the connected wallet is being returned
    } else {
        $('#enableMetamask').attr('disabled', true)
        $('#metaicon').removeClass('meta-normal')
        $('#metaicon').addClass('meta-gray')
        $('#enableMetamask').html("Install Metamask");
    }

    $('.pay-now').click(function () {
        paymentAddress = $(this).data("address");
        paymentRate = $(this).data("rate");
        paymentNetwork = $(this).data("chain");
        $("#paymentTypeChooseModal").modal()
    });

    $('.pay-native').click(function (e) {
        const address = $("#payment-address").val();
        const amount = $("#cryptoAmount").val();
        const network = $("#cryptoNetwork").val();
        console.log(leftAmount);
        let rate = $('#rate').val();
        let finalAmount = parseFloat(rate) * parseFloat(amount);

        if (amount === '') {
            e.preventDefault();
            Swal.fire(
                'Error',
                'Amount field required',
                'error')
        }
        if (finalAmount > leftAmount){
            e.preventDefault();
            Swal.fire(
                'Error',
                'Over draft',
                'error')
        }
        if (finalAmount < leftAmount && amount !== ''){
            checkMetamaskNetwork(network);
            makeMetamaskTransaction(address, amount, network);
        }
    });

    $('.pay-token').click(function (e) {
        const amount = $("#assetAmount").val();
        const tokenAddress = $("#assetAddress").val();
        if (amount === '') {
            e.preventDefault();
            Swal.fire(
                'Error',
                'Amount field required',
                'error').then(r => console.log(r));
        }
        if (amount > leftAmount){
            e.preventDefault();
            Swal.fire(
                'Error',
                'Over draft',
                'error').then(r => console.log(r));
        }
        if (amount <= leftAmount && amount !== ''){
            sendToken(paymentAddress, amount, tokenAddress);
        }
    });

    $('#cryptoAmount').on('input', function (e) {
        calculateCryptoAmount(e);
    });

    $('.native-option').click(function () {
        //const rate = paymentRate;//$(this).data("rate");
        //const address = paymentAddress;//$(this).data("address");
        //const chainId = paymentNetwork;//$(this).data("chain");
        $("#rate").val(paymentRate);
        $("#payment-address").val(paymentAddress);
        $("#cryptoNetwork").val(paymentNetwork);
        $("#cryptoAmount").val("");
        $("#cryptoTotal").val("");
        $('.modal').modal('hide');
        $("#paymentNativeModal").modal();
    });

    $('.asset-option').click(function () {
        $('.modal').modal('hide');
        $("#paymentAssetModal").modal();
    });


    /*const assetNameSelector = document.querySelector("#assetAddress");
    for (const contractAddress in contracts) {
        let option = document.createElement("option");
        if (contracts[contractAddress].erc20) {
            option.value = contractAddress;
            option.text = contracts[contractAddress].symbol + "-" + contracts[contractAddress].name;
            //console.log(contractAddress + "-"+contracts[contractAddress].symbol);
            option.setAttribute('data-tokens', contracts[contractAddress].symbol);
            assetNameSelector.appendChild(option);
        }
    }*/
}

function detectMetaMask() {
    return typeof window.ethereum !== 'undefined';
}

function handleAccountsChanged(accounts) {
    if (accounts.length === 0) {
        connectAccount();
    } else if (accounts[0] !== currentAccount) {
        currentAccount = accounts[0];
        getBalance(currentAccount)
        $('#metamask-status').html('');
        if (currentAccount != null) {
            $('#enableMetamask').html(currentAccount);
        }
    }
}

function getBalance(address) {

    web3.eth.getBalance(address, function (err, result) {
        if (err) {
            console.log(err)
        } else {
            let accountBalance = parseFloat(web3.utils.fromWei(result, "ether"));
            if (typeof accountBalance == 'number' && !isNaN(accountBalance)) {
                if (!Number.isInteger(accountBalance)) {
                    $('#account-balance').html(accountBalance.toFixed(4));
                }
            }
        }
    }).then(r  => console.log("Result ", r));

}

async function getContractBalance(address) {
    const contract = new web3.eth.Contract(ERC20_ABI, STABLE_COIN_ADDRESS.HOUSE_COIN_ADDRESS);
    const balance = await contract.methods.balanceOf(address).call();
    $('#tether-balance').html("HSCn " + parseFloat(balance) / 100);
}

async function sendToken(address, value, tokenAddress) {
    const contract = new web3.eth.Contract(ERC20_ABI, tokenAddress);
    const tokenDecimal = await contract.methods.decimals().call();
    let amount = value * Math.pow(10, tokenDecimal);

    ethereum
        .request({
            method: 'eth_sendTransaction',
            params: [
                {
                    from: currentAccount,
                    to: tokenAddress,
                    data: contract.methods.transfer(address, amount).encodeABI()
                },
            ],
        })
        .then((txHash) => {
            console.log("Contract Transaction hash ", txHash);
            $('.modal').modal('hide');
        })
        .catch((err) => {
            console.error("Error ", err);
        });
}

function checkMetamaskNetwork(chainId) {
    if (window.ethereum.networkVersion !== chainId) {
        ethereum
            .request({
                method: 'wallet_switchEthereumChain',
                params: [
                    {
                        chainId: web3.utils.toHex(chainId)
                    },
                ],
            })
            .catch((err) => {
                if (err.code === 4902) {
                    ethereum
                        .request({
                            method: 'wallet_addEthereumChain',
                            params: [
                                {
                                    chainId: web3.utils.toHex(chainId)
                                },
                            ],
                        })
                } else if (err.code === 4001) {
                    console.log('Change Network Error');
                    $('#paymentNativeModal').modal('hide');
                }
                console.log(err)
            });
    }
}

function connectAccount() {
    console.log('Calling connect()')
    ethereum
        .request({method: 'eth_requestAccounts'})
        .then(handleAccountsChanged)
        .catch((err) => {
            if (err.code === 4001) {
                console.log('You refused to connect Metamask');
                $('#metamask-status').html('You refused to connect Metamask')
            } else {
                console.error("Connection error => ", err);
            }
        });
}

function makeMetamaskTransaction(address, amount, network) {
    let amountDecimal = "0x0";
    if (amount !== "") {
        let amountWei = web3.utils.toWei(amount, 'ether');
        amountDecimal = web3.utils.toHex(amountWei);
    }
    ethereum
        .request({
            method: 'eth_sendTransaction',
            params: [
                {
                    from: currentAccount,
                    to: address,
                    value: amountDecimal,
                    chainId: network,
                },
            ],
        })
        .then((txHash) => {
            console.log("Transaction hash ", txHash);
        })
        .catch((err) => {
            if (err.code === 4001) {
                console.log('Please connect to MetaMask...');
                connectAccount();
            } else {
                console.error("Error ", err);
            }
        });
    $('.modal').modal('hide');
}

const isMetaMaskInstalled = () => {
    const {ethereum} = window
    return Boolean(ethereum && ethereum.isMetaMask)
}

const initialize = async () => {
    await openPaymentWidget()

    if (isMetaMaskInstalled()) {
        window.ethereum.on('accountsChanged', function (accounts) {
            console.log("Account changed");
            handleAccountsChanged(accounts);
        })

        window.ethereum.on('chainChanged', function (networkId) {
            web3 = new Web3(window.ethereum);
            getBalance(currentAccount)
        })
    }
}

window.addEventListener('DOMContentLoaded', initialize)