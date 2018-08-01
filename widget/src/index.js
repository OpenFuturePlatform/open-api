import * as internalWeb3 from 'web3';
import * as style from './style.css';

const _web3 = new internalWeb3(internalWeb3.givenProvider || 'http://localhost:8545');
let   windgetData;
let   userInputData = [];
let   currentNetwork;

//get data from server
async function getContractData(URI){
  let response = await fetch(URI, {
    method: 'GET',
    headers: new Headers({
      'Content-Type': 'application/json;charset=utf-8;'
    })
  });

  return await response.json();
};


//browser detect
// Chrome
const isChrome = !!window.chrome && !!window.chrome.webstore;
// Opera 8.0+
const isOpera = (!!window.opr && !!opr.addons) || !!window.opera || navigator.userAgent.indexOf(' OPR/') >= 0;
// Firefox 1.0+
const isFirefox = typeof InstallTrigger !== 'undefined';
// Blink engine detection
const isBlink = (isChrome || isOpera) && !!window.CSS;

const browserIsSupported = [isChrome, isOpera, isFirefox, isBlink].some(elem => elem);



//detect active network
const isMainNetwork = (windgetData) => {
  const cntrData = windgetData;

  return new Promise((resolve, reject) => {
    window.web3.version.getNetwork((err, netId) => {
      if (err) {
        reject(err);
        return;
      }
      currentNetwork = netId;
      netId === '4' || netId === '1' ? resolve() : reject('selesc main (or renkiby network for tests)');
    });
  }).then(()=>{
    sendToContract(cntrData);
  }).catch((err) => {console.log(err)})
};

//sent eth to developer
async function sendToContract(cntrData){
  const defaultAccount = window.web3.eth.defaultAccount;
  let button = document.getElementById('submit');
  if(!defaultAccount){
    button.textContent = 'install and login in metamask pluggin';
    return;
  }
  
  const scaffoldContract = new _web3.eth.Contract(
    JSON.parse(cntrData.abi), 
    cntrData.address,
    {from: defaultAccount}
  )   
  
  scaffoldContract.methods.payDeveloper(...userInputData).send({value:_web3.utils.toWei(windgetData.conversionAmount, 'ether')}).then((TxHash) => {
    let network = currentNetwork === '4'? 'rinkeby.' : '';
    button.removeEventListener('click', sendToScaffoldEvent,false);
        button.textContent = '';
    var link = document.createElement('a');
        link.textContent = 'VIEW TRANSACTION STATUS';
        link.setAttribute('id','submit');
        link.setAttribute('target','_blank');
        link.href = `https://${network}etherscan.io/tx/${TxHash.transactionHash}`;

    button.appendChild(link);
  })
  
button.textContent = 'SENDING. PLEASE WAIT...';

}

function validateForm(){
  userInputData = [];
  let valid = true;
  let elementCtn = document.forms.openWidgetForm.elements.length - 1;

  for (let i = 0; i < elementCtn; i++) {
    const formElem = document.getElementsByClassName('card-input')[i];
    if(formElem.value){ 
      userInputData.push(toHex(formElem.value));
    }else{
      document.getElementsByClassName('form__label')[i].className = 'form__label required-field';
      valid = false;
    }
  };

  if(!valid) userInputData = [];

  return valid;
};



async function openWidget(scaffold){
  const scaffoldAddress =  scaffold || await window.parent.document.getElementById('open-widget-iframe').dataset.address;
  const OPEN_URL  = `https://api.open-platform.zensoft.io/widget/scaffolds/${scaffoldAddress}`;
  windgetData = await getContractData(OPEN_URL);

  let widgetEl = document.createElement('div');
  widgetEl.setAttribute('id','open-widget');
  let conteiner = document.body.appendChild(widgetEl);


  //if browser not support Ethereum Ðapps
  if(!browserIsSupported){
    let warn = document.createElement('h3');
    warn.setAttribute('class','warn');
    warn.textContent = `Oops! Your browser does not support Ethereum Ðapps.`;
    conteiner.appendChild(warn);
  }
  


  let form = document.createElement('form');
  form.setAttribute('name','openWidgetForm');

  //create scaffold description
  let desc = document.createElement('h3');
  desc.textContent = `${windgetData.description}`;
  form.appendChild(desc);

  let currAmount = document.createElement('span');
  currAmount.setAttribute('class','open-amount');
  currAmount.innerHTML   += `<b>${windgetData.currency}</b> ${windgetData.fiatAmount}</br>`;
  currAmount.innerHTML   += `<b>ETH </b> ${windgetData.conversionAmount}`;
  form.appendChild(currAmount);

  let propsCount = 0;
  for(let prop of windgetData.properties){
    
      let defaultVal = (prop.defaultValue === 'null') ? '': prop.defaultValue;
      let nameWithSpaces = prop.name.replace(/_|-|\./g, ' ');
      let l = document.createElement("LABEL");
      l.setAttribute('class','form__label');
      l.setAttribute('name',`formLabel${propsCount}`);
      l.setAttribute('for',`formInput${propsCount}`);
      l.textContent = `${nameWithSpaces}`;
      form.appendChild(l);
    
    if(prop.type === 'BOOLEAN'){

      //Create array of options to be added
      var choiceArray = ['No','Yes'];

      //Create and append select list
      var selectList = document.createElement('select');
      selectList.setAttribute('id',`formInput${propsCount}`);
      selectList.setAttribute('class','card-input');


      //Create and append the options
      for (let i = 0; i < choiceArray.length; i++) {
          let option = document.createElement('option');
          option.setAttribute('value', choiceArray[i]);
          if(!i) option.setAttribute('selected', '');
          option.text = choiceArray[i];
          selectList.appendChild(option);
      }
      
      form.appendChild(selectList);

      
    }else{
      
      let i = document.createElement('input');
      i.setAttribute('class','card-input');
      i.setAttribute('type',`${htmlType(prop.type)}`);
      i.setAttribute('id',`formInput${propsCount}`);
      i.setAttribute('name',`${prop.name}`);
      i.setAttribute('placeholder',`${nameWithSpaces}`);
      i.setAttribute('required', '');
      if(defaultVal) i.setAttribute('value',`${defaultVal}`);
      form.appendChild(i);
      propsCount++;
    }
  }

  let s = document.createElement('button');
  s.setAttribute('id','submit');
  s.textContent = `Pay ${windgetData.conversionAmount} ETH`;
  s.addEventListener('click', sendToScaffoldEvent,false);
  form.appendChild(s);

  //add form in to widget
  conteiner.appendChild(form);

}

function sendToScaffoldEvent(e){
  e.preventDefault();
  if( validateForm()){
    return isMainNetwork(windgetData);
  }   
    return false;
}

function htmlType(type){
  switch(type) {
    case 'STRING':
      return 'text';
      break;

    case 'NUMBER':
      return 'number';
      break;

    case 'BOOLEAN':
      return 'checkbox';
      break;
  }
};

function toHex(data){
    return _web3.utils.toHex(data);
}

openWidget('0x3D788EDDf0c5B71dF0f19b97b1360611CbDF5cDa')