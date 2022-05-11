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
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.1/css/bootstrap-select.css"/>
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
                    <img src="https://api.openfuture.io/img/landing_new/logo.svg" alt="OpenPlatform Logo">
                </a>
            </div>
            <div class="container-fluid">
                <div class="pull-right" style="text-align: right">
                    <img src="/static/images/metamask-fox.svg" alt="Metamask Logo" id="metaicon">
                    <span id="metamask-status"></span>
                    <button id="enableMetamask" class="btn alert alert-primary">Connect with Metamask</button>
                    <span id="account-balance"  class="btn alert alert-secondary"></span>
                    <!--<span id="tether-balance"  class="btn alert alert-success"></span>
                    <span id="usdc-balance"  class="btn alert alert-success"></span>
                    <span id="busd-balance"  class="btn alert alert-success"></span>-->
                </div>
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
<div class="modal fade" id="paymentNativeModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span>
                </button>
            </div>
            <div class="modal-body">

                    <div class="form-group">
                        <label for="payment-address">Payment Address </label>
                        <input type="text" class="form-control" id="payment-address" readonly>
                    </div>
                    <div class="form-group">
                        <label for="rate">Current Rate</label>
                        <input type="text" class="form-control" id="rate" readonly>
                    </div>
                    <div class="form-group">
                        <label for="cryptoAmount">Amount</label>
                        <input type="text" class="form-control" id="cryptoAmount">
                    </div>
                    <div class="form-group">
                        <label for="cryptoTotal">Total Amount</label>
                        <input type="text" class="form-control" id="cryptoTotal" readonly>
                    </div>
                    <input type="hidden" class="form-control" id="cryptoNetwork">
                    <button class="btn btn-success btn-block pay-native">
                        Pay
                    </button>

            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="paymentTypeChooseModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Choose payment type</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">

                    <button class="btn btn-primary btn-block native-option">
                        Pay
                    </button>
                    <button class="btn btn-success btn-block asset-option">
                        Pay with Tokens
                    </button>

            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="paymentAssetModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label for="assetAddress">Custom Tokens(ERC20)</label>
                    <select class="form-control selectpicker" data-live-search="true" id="assetAddress">
                        <option data-tokens="HSCn" value="0x6Bf8526b51D4A1601Fed1046f13Dbf5aC663028E">House Coin</option>
                        <option data-tokens="USDT" value="0xdAC17F958D2ee523a2206206994597C13D831ec7">Tether USD</option>
                        <option data-tokens="USDC" value="0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48">USD Coin</option>
                        <option data-tokens="BUSD" value="0x4Fabb145d64652a948d72533023f6E7A623C7C53">Binance USD</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="assetAmount">Amount</label>
                    <input type="text" class="form-control" id="assetAmount">
                </div>
                <button class="btn btn-success btn-block pay-token">
                    Pay with Token
                </button>
            </div>
        </div>
    </div>
</div>

<script src="${host}/static/js/payment-chooser.js"></script>
</body>
</html>