<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <!--<meta http-equiv="refresh" content="15" />-->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://common.olemiss.edu/_js/datatables/media/js/jquery.dataTables.js"></script>
    <title></title>
</head>
<body data-value="${address}" data-host="${host}" data-type="${type}">

<div class="container">
    <header class="mb-4">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <a href="https://www.openfuture.io/"><img src="https://api.openfuture.io/img/landing_new/logo.svg"></a>
            </div>
        </nav>
    </header>
    <div class="row mb-4">
        <div class="col-md-6 mb-3 mb-md-0">
            <div class="card h-100">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h2 class="card-header-title">Overview</h2>
                </div>
                <div class="card-body">
                    <div class="row align-items-center">
                        <div class="col-md-4 mb-1 mb-md-0">Amount:</div>
                        <div class="col-md-8 amount">0.677271</div>
                    </div>
                    <hr class="hr-space">
                    <div class="row">
                        <div class="col-md-4 mb-1 mb-md-0">Remaining:</div>
                        <div class="col-md-8 remaining">0.677171</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div hidden id="spinner"></div>
    <div class="card">
        <div class="card-header">Transactions</div>
        <div class="card-body">
            <div class="table-responsive mb-2 mb-md-0">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Txn Hash</th>
                            <th>From</th>
                            <th>To</th>
                            <th>Value</th>
                            <th>Blockchain</th>
                        </tr>
                    </thead>
                    <tbody class="transactions">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="${host}/static/js/payment-widget.js"></script>
</body>
</html>