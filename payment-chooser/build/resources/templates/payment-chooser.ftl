<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/js/bootstrap-select.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>

    <title></title>
</head>
<body data-order="${orderKey}" data-host="${host}" data-currency="${currency}">

<div class="container">
    <header class="mb-4">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <a href="https://www.openfuture.io/">
                    <img src="https://api.openfuture.io/img/landing_new/logo.svg">
                </a>
            </div>
        </nav>
    </header>
    <div class="row">
        <div class="col-md-4">
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h2 class="card-header-title">Your Order</h2>
                </div>
                <div class="card-body">
                    <div class="row align-items-left">
                        <div class="col-md-6 mb-1 mb-md-0">Amount:</div>
                        <div class="col-md-6 amount"></div>
                    </div>
                    <hr class="hr-space">
                    <div class="row align-items-left">
                        <div class="col-md-6 mb-1 mb-md-0">Remaining:</div>
                        <div class="col-md-6 remaining"></div>
                    </div>
                </div>
                <div id="countdown"></div>
            </div>

        </div>
        <div class="col-md-8 mb-3 mb-md-0">
            <div hidden id="spinner"></div>
            <div class="accordion" id="accordionExample">

            </div>
        </div>
    </div>

</div>

<script src="${host}/static/js/payment-chooser.js"></script>
</body>
</html>