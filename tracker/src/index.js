import * as style from './style.css';

const walletValue = document.body.dataset.value;
const host = document.body.dataset.host;
const OPEN_URL = `${host}/widget/transactions/address/${walletValue}`;

const spinner = document.getElementById("spinner");

function fetchData() {
    spinner.setAttribute('hidden', '');
    $.ajax({
        url: OPEN_URL,
        type: 'get',
        success: function (data) {

            setTimeout(function () {
                spinner.removeAttribute('hidden');

            }, 3000);

            loadData(data);
        },
        complete: function (data) {
            setTimeout(fetchData, 10000);
        }
    });
}

function loadData(result) {
    console.log(result);

    //// Transactions
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = "";
    for (let tx of result) {

        let row = document.createElement('tr');
        let row_data_1 = document.createElement('td');
        row_data_1.innerHTML = `<b>${tx.blockHash}</b>`;
        let row_data_2 = document.createElement('td');
        row_data_2.innerHTML = `<b>${tx.from}</b>`;
        let row_data_3 = document.createElement('td');
        row_data_3.innerHTML = `<b>${tx.to}</b>`;
        let row_data_4 = document.createElement('td');
        row_data_4.innerHTML = `<b>${tx.amount}</b>`;
        let row_data_5 = document.createElement('td');
        row_data_5.innerHTML = `<b>${tx.native}</b>`;
        let row_data_6 = document.createElement('td');
        if (tx.token != null)
            row_data_6.innerHTML = `<b>${tx.token}</b>`;

        row.appendChild(row_data_1);
        row.appendChild(row_data_2);
        row.appendChild(row_data_3);
        row.appendChild(row_data_4);
        row.appendChild(row_data_5);
        row.appendChild(row_data_6);
        tableBody.appendChild(row);
    }
}

fetchData();