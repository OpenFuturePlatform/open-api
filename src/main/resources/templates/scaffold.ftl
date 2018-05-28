pragma solidity ^0.4.19;

contract OpenScaffold {
    // on-chain transaction storage
    struct OpenScaffoldTransaction {
        address customerAddress;
        ${SCAFFOLD_STRUCT_PROPERTIES}
    }

    // events
    event paymentComplete(
        address customerAddress,
        uint transactionAmount,
        uint scaffoldTransactionIndex,
        ${CUSTOM_SCAFFOLD_PARAMETERS}
        );
    event fundsDeposited(uint _amount);
    event incorrectVendorAddress(address requestAddress, address vendorAddress);

    // custom dataTypes - array for storage of transactions
    OpenScaffoldTransaction[] public openScaffoldTransactions;

    // constructor variables
    address public vendorAddress;
    string public scaffoldDescription;
    string public fiatAmount;
    string fiatCurrency;
    uint public scaffoldAmount;

    // generated internally by contract
    uint public scaffoldTransactionIndex;
    address private scaffoldAddress = this;


    function OpenScaffold(
        address _vendorAddress,
        string _description,
        string _fiatAmount,
        string _fiatCurrency,
        uint _scaffoldAmount
    )
        public
    {
        vendorAddress = _vendorAddress;
        scaffoldDescription = _description;
        fiatAmount = _fiatAmount;
        fiatCurrency = _fiatCurrency;
        scaffoldAmount = _scaffoldAmount;
    }

    function payVendor(${CUSTOM_SCAFFOLD_PARAMETERS}) public payable {
        require(msg.value == scaffoldAmount);
        scaffoldTransactionIndex++;

        address customerAddress = msg.sender;
        uint transactionAmount = msg.value;

        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
            customerAddress: customerAddress,
            ${SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS}
            });

        openScaffoldTransactions.push(newTransaction);

        paymentComplete(
            customerAddress,
            transactionAmount,
            scaffoldTransactionIndex,
            ${CUSTOM_RETURN_VARIABLES}
            );
    }

    function withdrawFunds(uint amount) public returns(bool) {
            if(msg.sender != vendorAddress) {
                incorrectVendorAddress(msg.sender, vendorAddress);
                return false;
            }

            if(amount > scaffoldAddress.balance) {
                return false;
            }

            // transfer amount
            vendorAddress.transfer(amount);
            fundsDeposited(amount);
            return true;
    }

    function getScaffoldSummary() public view returns (string, uint, string, string, uint, uint, address) {
        return (
          scaffoldDescription,
          scaffoldAddress.balance,
          fiatAmount,
          fiatCurrency,
          scaffoldAmount,
          scaffoldTransactionIndex,
          vendorAddress
        );
    }

}