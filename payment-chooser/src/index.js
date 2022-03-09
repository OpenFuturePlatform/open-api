import * as style from './style.css';

let   widgetData;


//get data from server
async function getAddressData(URI){
    let response = await fetch(URI, {
        method: 'GET',
        headers: new Headers({
            'Content-Type': 'application/json;charset=utf-8;'
        })
    });

    return await response.json();
}

async function openPaymentWidget(){
    const orderKey = document.body.dataset.order;
    const host = document.body.dataset.host;
    const OPEN_URL = `${host}/widget/payment/addresses/order/${orderKey}`;

    widgetData = await getAddressData(OPEN_URL);
    console.log(widgetData.orderDate);
    countdownTimer(new Date(`${widgetData.orderDate}`).getTime())

    const accordion = document.querySelector(".accordion");
    let i = 1;
    for (let blockchain of widgetData.wallets) {
        let card = document.createElement('div');
        card.setAttribute('class','card');

        let cardHeader = document.createElement('div');
        cardHeader.setAttribute('class','card-header');
        cardHeader.setAttribute('id','headingOne');

        let cardHeaderH5 = document.createElement('h5');
        cardHeaderH5.setAttribute('class','mb-0');
        let cardHeaderH5Button = document.createElement('button');
        cardHeaderH5Button.setAttribute('class','btn btn-link');
        cardHeaderH5Button.setAttribute('type','button');
        cardHeaderH5Button.setAttribute('data-toggle','collapse');
        cardHeaderH5Button.setAttribute('data-target','#collapse'+i+'b');
        cardHeaderH5Button.setAttribute('aria-expanded','true');
        cardHeaderH5Button.setAttribute('aria-controls','collapse'+i+'b');

        let blockChain = getBlockchainObject(blockchain);
        let cardHeaderH5img = document.createElement('img');
        cardHeaderH5img.setAttribute('class','align-middle blockchain-icon');
        cardHeaderH5img.setAttribute('src', blockChain['imgSrc']);

        cardHeaderH5Button.innerHTML = blockChain['title'];
        cardHeaderH5Button.appendChild(cardHeaderH5img);
        cardHeaderH5.appendChild(cardHeaderH5Button);
        cardHeader.appendChild(cardHeaderH5);
        card.appendChild(cardHeader);

        let cardDiv = document.createElement('div');
        cardDiv.setAttribute('id','collapse'+i+'b');
        cardDiv.setAttribute('class','collapse');
        cardDiv.setAttribute('data-parent','#accordionExample');
        let cardDivBody = document.createElement('div');
        cardDivBody.setAttribute('class','card-body');
        let cardDivBodyQrCode = document.createElement('div');

        let qrCodeId = 'qrcode'+i+'b';
        cardDivBodyQrCode.setAttribute('id',qrCodeId);

        cardDivBody.appendChild(cardDivBodyQrCode);
        let cardDivBodyAddress = document.createElement('div');
        cardDivBodyAddress.setAttribute('class','address');
        let cardDivBodyAddressHref = document.createElement('a');
        const TRANSACTION_URL = `${host}/widget/transactions/address/${blockchain.address}`;
        cardDivBodyAddressHref.setAttribute('href',TRANSACTION_URL);
        cardDivBodyAddressHref.setAttribute('target','_blank');
        cardDivBodyAddressHref.innerHTML = blockchain.address;
        cardDivBodyAddress.appendChild(cardDivBodyAddressHref);
        cardDivBody.appendChild(cardDivBodyAddress);
        cardDiv.appendChild(cardDivBody);
        card.appendChild(cardDiv);

        accordion.appendChild(card);
        console.log(blockchain);

        new QRCode(qrCodeId, {
            text: blockchain.address,
            colorDark : "#000000",
            colorLight : "#ffffff",
            correctLevel : QRCode.CorrectLevel.H
        });
        i++;

    }
}

function getBlockchainObject(blockchain){
    let IDs = {};
    if (blockchain.blockchain === "BTC"){
        IDs['imgSrc'] = "/static/images/BTC.png";
        IDs['title']  = "Bitcoin";
    } else if (blockchain.blockchain === "ETH"){
        IDs['imgSrc'] = "/static/images/ETH.png";
        IDs['title']  = "Ethereum";
    } else {
        IDs['imgSrc'] = "/static/images/BNB.png";
        IDs['title']  = "Binance";
    }

    return IDs;
}

function countdownTimer(countDownDate){

        let x = setInterval(function() {

            let now = new Date().getTime();
            let distance = countDownDate - now;

            let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            let seconds = Math.floor((distance % (1000 * 60)) / 1000);

            document.getElementById("countdown").innerHTML = hours + ":" + minutes + ":" + seconds;

            if (distance < 0) {
                clearInterval(x);
                document.getElementById("countdown").innerHTML = "EXPIRED";
            }
        }, 1000);
}

openPaymentWidget()