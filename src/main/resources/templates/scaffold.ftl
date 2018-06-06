pragma solidity ^0.4.19;

/**
 * @title SafeMath
 * @dev Math operations with safety checks that throw on error
 */

library SafeMath {

    function mul(uint256 a, uint256 b) internal constant returns (uint256) {
        uint256 c = a * b;
        assert(a == 0 || c / a == b);
        return c;
    }

    function div(uint256 a, uint256 b) internal constant returns (uint256) {
        // assert(b > 0); // Solidity automatically throws when dividing by 0
        uint256 c = a / b;
        // assert(a == b * c + a % b); // There is no case in which this doesn't hold
        return c;
    }

    function sub(uint256 a, uint256 b) internal constant returns (uint256) {
        assert(b <= a);
        return a - b;
    }

    function add(uint256 a, uint256 b) internal constant returns (uint256) {
        uint256 c = a + b;
        assert(c >= a);
        return c;
    }

}

/**
 * @title ERC20Basic
 * @dev Simpler version of ERC20 interface
 */
contract ERC20Token {
  function balanceOf(address who) public constant returns (uint256);
  function transfer(address to, uint256 value) public returns (bool);
}

contract OpenScaffold {

    using SafeMath for uint256;

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
    event fundsDeposited(uint _amount, address _toAddress);
    event activatedScaffold(bool activated);

    // custom dataTypes - array for storage of transactions
    OpenScaffoldTransaction[] public openScaffoldTransactions;

    // constructor variables
    address public  vendorAddress;
    address private developerAddress;
    string public   scaffoldDescription;
    string public   fiatAmount;
    string          fiatCurrency;
    uint public     scaffoldAmount;

    // generated internally by contract
    uint public scaffoldTransactionIndex;
    address private scaffoldAddress = this;

    // OPEN token
    uint constant private ACTIVATING_TOKENS_AMOUNT = 10 * 10**8;
    address constant private OPEN_TOKEN_ADDRESS = ${OPEN_TOKEN_ADDRESS};
    ERC20Token public OPENToken = ERC20Token(OPEN_TOKEN_ADDRESS);


    // Throws if called by any account other than the developer.
    modifier onlyVendor() {
        require(msg.sender == developerAddress);
        _;
    }

    // Throws if contract is not activated.
    modifier activated() {
        require(OPENToken.balanceOf(address(this)) >= ACTIVATING_TOKENS_AMOUNT);
        _;
    }


    function OpenScaffold(
        address _vendorAddress,
        address _developerAddress,
        string _description,
        string _fiatAmount,
        string _fiatCurrency,
        uint _scaffoldAmount
    )
        public
    {
        vendorAddress = _vendorAddress;
        developerAddress = _developerAddress;
        scaffoldDescription = _description;
        fiatAmount = _fiatAmount;
        fiatCurrency = _fiatCurrency;
        scaffoldAmount = _scaffoldAmount;
    }

    // deactivate Scaffold contract by vendor
    function deactivate() onlyVendor public activated {
        OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));
        activatedScaffold(false);
    }

    function(${CUSTOM_SCAFFOLD_PARAMETERS}) public payable activated {
        require(msg.value == scaffoldAmount);
        scaffoldTransactionIndex++;

        address customerAddress = msg.sender;
        uint256 transactionAmount = msg.value;
        // developer fee
        uint256 developerFee = transactionAmount.div(100).mul(3);
        uint256 vendorAmount = transactionAmount.sub(developerFee);

        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
            customerAddress: customerAddress,
            ${SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS}
            });

        openScaffoldTransactions.push(newTransaction);

        // transfer amount
        withdrawFunds(vendorAddress, vendorAmount);
        withdrawFunds(developerAddress, developerFee);

        paymentComplete(
            customerAddress,
            vendorAmount,
            scaffoldTransactionIndex,
            ${CUSTOM_RETURN_VARIABLES}
            );
    }

    function withdrawFunds(address to, uint amount) private {
        to.transfer(amount);
        fundsDeposited(amount, to);
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