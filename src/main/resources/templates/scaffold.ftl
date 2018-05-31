pragma solidity ^0.4.19;

/**
 * @title ERC20Basic
 * @dev Simpler version of ERC20 interface
 */
contract ERC20Token {
  function balanceOf(address who) public constant returns (uint256);
  function transfer(address to, uint256 value) public returns (bool);
}

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
    event activatedScaffold(bool activated);

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

    // OPEN token
    uint constant private activatingTokensAmount = 10 * 10**8;
    address constant private openTokenAddress = 0x69c4BB240cF05D51eeab6985Bab35527d04a8C64;
    ERC20Token public OPENToken = ERC20Token(openTokenAddress);

    // Throws if called by any account other than the developer.
    modifier onlyVendor() {
        require(msg.sender == vendorAddress);
        _;
    }

    // Throws if contract is not activated.
    modifier activated() {
        require(OPENToken.balanceOf(address(this)) >= activatingTokensAmount);
        _;
    }


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

    // deactivate Scaffold by
    function deactivate() onlyVendor public activated {
        OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));
        activatedScaffold(false);
    }

    function(${CUSTOM_SCAFFOLD_PARAMETERS}) public payable activated {
        require(msg.value == scaffoldAmount);
        scaffoldTransactionIndex++;

        address customerAddress = msg.sender;
        uint transactionAmount = msg.value;

        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
            customerAddress: customerAddress,
            ${SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS}
            });

        openScaffoldTransactions.push(newTransaction);

        // transfer amount
        withdrawFunds(transactionAmount);

        paymentComplete(
            customerAddress,
            transactionAmount,
            scaffoldTransactionIndex,
            ${CUSTOM_RETURN_VARIABLES}
            );
    }

    function withdrawFunds(uint amount) private {
        vendorAddress.transfer(amount);
        fundsDeposited(amount);
    }

    function getScaffoldSummary() public view returns (string, uint, string, string, uint, uint, address, uint) {
        return (
          scaffoldDescription,
          scaffoldAddress.balance,
          fiatAmount,
          fiatCurrency,
          scaffoldAmount,
          scaffoldTransactionIndex,
          vendorAddress,
          OPENToken.balanceOf(address(this))
        );
    }

}