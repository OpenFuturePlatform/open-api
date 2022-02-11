
import * as style from './style.css';

let   windgetData;

//get data from server
async function getTransactionData(URI){
  let response = await fetch(URI, {
    method: 'GET',
    headers: new Headers({
      'Content-Type': 'application/json;charset=utf-8;'
    })
  });

  return await response.json();
}

async function paymentWidget(){
  const walletAddress = await document.body.dataset.address;
  const host = await document.body.dataset.host;
  const OPEN_URL  = `${host}/widget/transactions/${walletAddress}`;
  windgetData = await getTransactionData(OPEN_URL);

  let widgetEl = document.createElement('div');
      widgetEl.setAttribute('class','card');

  let cardHeader = document.createElement('div');
  cardHeader.setAttribute('class','card-header');
  cardHeader.innerHTML = "Transactions";
  widgetEl.appendChild(cardHeader);

  let cardBody = document.createElement('div');
  cardBody.setAttribute('class','card-body');
  widgetEl.appendChild(cardBody);

  let container = document.body.appendChild(widgetEl);

  let form = document.createElement('div');
  form.setAttribute('class','table-responsive mb-2 mb-md-0');

  let table = document.createElement('table');
  table.setAttribute('class','table table-hover');
  let thead = document.createElement('thead');
  thead.setAttribute('class','thead-light');
  let tbody = document.createElement('tbody');

  // Creating and adding data to first row of the table
  let row_1 = document.createElement('tr');
  let heading_1 = document.createElement('th');
  heading_1.innerHTML = "Txn Hash";
  let heading_2 = document.createElement('th');
  heading_2.innerHTML = "From";
  let heading_3 = document.createElement('th');
  heading_3.innerHTML = "Value";

  row_1.appendChild(heading_1);
  row_1.appendChild(heading_2);
  row_1.appendChild(heading_3);
  thead.appendChild(row_1);

  // Creating and adding data to second row of the table
  for(let prop of windgetData){
    let row_2 = document.createElement('tr');
    let row_2_data_1 = document.createElement('td');
    row_2_data_1.innerHTML = `<b>${prop.blockHash}</b>`;
    let row_2_data_2 = document.createElement('td');
    row_2_data_2.innerHTML = `<b>${prop.from}</b>`;
    let row_2_data_3 = document.createElement('td');
    row_2_data_3.innerHTML = `<b>${prop.amount}</b>`;

    row_2.appendChild(row_2_data_1);
    row_2.appendChild(row_2_data_2);
    row_2.appendChild(row_2_data_3);
    tbody.appendChild(row_2);
  }

  table.appendChild(thead);
  table.appendChild(tbody);

  form.appendChild(table);

  cardBody.appendChild(form);

}

paymentWidget()