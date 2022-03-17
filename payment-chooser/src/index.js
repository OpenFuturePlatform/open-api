import * as style from './style.css';

let widgetData;

const spinner = document.getElementById("spinner");
const orderKey = document.body.dataset.order;
const host = document.body.dataset.host;
const currency = document.body.dataset.currency;
const OPEN_URL = `${host}/widget/payment/addresses/order/${orderKey}`;
let timerId = 1;

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

    const amount = document.querySelector(".amount");
    const remaining = document.querySelector(".remaining");
    amount.innerHTML = `${result.orderAmount}` + ` ${currency}`;
    //let leftAmount = Math.ceil(result.orderAmount - result.paid);
    let leftAmount = result.orderAmount - result.paid;
    remaining.innerHTML = `${leftAmount}` + ` ${currency}`;

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
            td_4.innerHTML = `${trx.rate}` + ` ${currency}`;

            rowBody.appendChild(td_1);
            rowBody.appendChild(td_2);
            rowBody.appendChild(td_3);
            rowBody.appendChild(td_4);
            tbody.appendChild(rowBody);
        }
    }
}

async function openPaymentWidget() {

    widgetData = await getAddressData(OPEN_URL);
    console.log(widgetData.orderDate);

    const amount = document.querySelector(".amount");
    const remaining = document.querySelector(".remaining");
    amount.innerHTML = `${widgetData.orderAmount}` + ` ${currency}`;
    //let leftAmount = Math.ceil(widgetData.orderAmount - widgetData.paid);
    let leftAmount = widgetData.orderAmount - widgetData.paid;
    remaining.innerHTML = `${leftAmount}` + ` ${currency}`;

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

        let cardDiv = document.createElement('div');
        cardDiv.setAttribute('id', 'collapse' + i + 'b');
        cardDiv.setAttribute('class', 'collapse');
        cardDiv.setAttribute('data-parent', '#accordionExample');
        let cardDivBody = document.createElement('div');
        cardDivBody.setAttribute('class', 'card-body');
        cardDivBody.setAttribute('id', `${blockchain.address}`);
        let cardDivBodyQrCode = document.createElement('div');

        let qrCodeId = 'qrcode' + i + 'b';
        cardDivBodyQrCode.setAttribute('id', qrCodeId);

        cardDivBody.appendChild(cardDivBodyQrCode);
        let cardDivBodyAddress = document.createElement('div');
        cardDivBodyAddress.setAttribute('class', 'address');
        let cardDivBodyAddressHref = document.createElement('a');
        cardDivBodyAddressHref.setAttribute('href', blockChain['explorerUrl']);
        cardDivBodyAddressHref.setAttribute('target', '_blank');
        cardDivBodyAddressHref.innerHTML = blockchain.address;
        cardDivBodyAddress.appendChild(cardDivBodyAddressHref);
        cardDivBody.appendChild(cardDivBodyAddress);

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

        new QRCode(qrCodeId, {
            text: blockchain.address,
            colorDark: "#000000",
            colorLight: "#ffffff",
            correctLevel: QRCode.CorrectLevel.H
        });
        i++;

    }

    fetchTransactionData();
}

function getBlockchainObject(blockchain) {
    let IDs = {};
    if (blockchain.blockchain === "BitcoinBlockchain") {
        IDs['imgSrc'] = "/static/images/BTC.png";
        IDs['title'] = "Bitcoin";
        IDs['explorerUrl'] = "https://www.blockchain.com/btc/address/"+blockchain.address;
    } else if (blockchain.blockchain === "EthereumBlockchain") {
        IDs['imgSrc'] = "/static/images/ETH.png";
        IDs['title'] = "Ethereum";
        IDs['explorerUrl'] = "https://etherscan.io/address/"+blockchain.address;
    } else if (blockchain.blockchain === "RopstenBlockchain") {
        IDs['imgSrc'] = "/static/images/ETH.png";
        IDs['title'] = "Ropsten";
        IDs['explorerUrl'] = "https://ropsten.etherscan.io/address/"+blockchain.address;
    } else if (blockchain.blockchain === "BinanceTestnetBlockchain") {
        IDs['imgSrc'] = "/static/images/BNB.png";
        IDs['title'] = "Binance Test";
        IDs['explorerUrl'] = "https://testnet.bscscan.com/address/"+blockchain.address;
    } else {
        IDs['imgSrc'] = "/static/images/BNB.png";
        IDs['title'] = "Binance";
        IDs['explorerUrl'] = "https://bscscan.com/address/"+blockchain.address;
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

openPaymentWidget()