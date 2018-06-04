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
    address constant private openTokenAddress = ${OPEN_TOKEN_ADDRESS};
    ERC20Token public OPENToken = ERC20Token(openTokenAddress);
    address private developerAddress;

    // Throws if called by any account other than the developer.
    modifier onlyVendor() {
        require(msg.sender == developerAddress);
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
        developerAddress = msg.sender;
    }

    // deactivate Scaffold contract by vendor
    function deactivate() onlyVendor public activated {
        OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));
        activatedScaffold(false);
    }

    function payVendor(${CUSTOM_SCAFFOLD_PARAMETERS}) public payable activated {
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

    function getScaffoldSummary() public view returns (string, string, string, uint, uint, address, uint) {
        return (
          scaffoldDescription,
          fiatAmount,
          fiatCurrency,
          scaffoldAmount,
          scaffoldTransactionIndex,
          vendorAddress,
          OPENToken.balanceOf(address(this))
        );
    }

}