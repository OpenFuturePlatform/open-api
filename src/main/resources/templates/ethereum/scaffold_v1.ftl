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

    // shareholder struct
    struct Partner {
        uint256 share;
        uint index;
    }


    // event protocol
    uint256 constant PAYMENT_COMPLETED = 1;
    uint256 constant FUNDS_DEPOSITED = 2;
    uint256 constant ACTIVATED_SCAFFOLD = 3;
    uint256 constant ADDED_SHARE_HOLDER = 4;
    uint256 constant EDITED_SHARE_HOLDER = 5;
    uint256 constant DELETED_SHARE_HOLDER = 6;
    uint256 constant PAID_FOR_SHARE_HOLDER = 7;

    // events
    event PaymentCompleted(
        uint256 _eventType,
        address _customerAddress,
        uint256 _transactionAmount,
        uint256 _scaffoldTransactionIndex,
        ${CUSTOM_SCAFFOLD_PARAMETERS}
    );
    event FundsDeposited(uint256 _eventType, uint256 _amount, address _toAddress);
    event ActivatedScaffold(uint256 _eventType, bool _activated);
    event ShareHolderEvent(uint256 _eventType, address _shareHolderAddress, uint256 _share);
    event PaidForShareHolder(uint256 _eventType, address _userAddress, uint256 _amount);


    // custom dataTypes
    // array for storage of transactions
    OpenScaffoldTransaction[] public openScaffoldTransactions;
    // array of shareholders addresses
    address[] public shareHolderAddresses;
    // mapping for storage of partners(shareholders)
    mapping(address => Partner) public partners;

    // constructor variables
    address public developerAddress;
    bytes32 public fiatAmount;
    bytes32 public fiatCurrency;
    uint256 public scaffoldAmount;
    address private platformAddress;

    // generated internally by contract
    uint256 public scaffoldTransactionIndex;
    // summarize all the shares that would not exceed the limit
    uint256 private totalAmountShares = 0;

    // OPEN token
    uint256 constant private ACTIVATING_TOKENS_AMOUNT = 10 * 10**8;
    address constant private OPEN_TOKEN_ADDRESS = ${OPEN_TOKEN_ADDRESS};
    ERC20Token public OPENToken = ERC20Token(OPEN_TOKEN_ADDRESS);


    // Throws if called by any account other than the developer or OPEN platform addresses.
    modifier onlyDeveloper() {
        require(developerAddress == msg.sender || platformAddress == msg.sender);
        _;
    }

    // Throws if contract is not activated.
    modifier activated() {
        require(ACTIVATING_TOKENS_AMOUNT <= OPENToken.balanceOf(address(this)));
        _;
    }


    // constructor
    function OpenScaffold(
        address _developerAddress,
        address _platformAddress,
        bytes32 _fiatAmount,
        bytes32 _fiatCurrency,
        uint256 _scaffoldAmount
    )
        public
    {
        developerAddress = _developerAddress;
        platformAddress = _platformAddress;
        fiatAmount = _fiatAmount;
        fiatCurrency = _fiatCurrency;
        scaffoldAmount = _scaffoldAmount;
    }

    // deactivate Scaffold contract
    function deactivate()
        public
        onlyDeveloper
        activated
    {
        OPENToken.transfer(developerAddress, OPENToken.balanceOf(address(this)));
        ActivatedScaffold(ACTIVATED_SCAFFOLD, false);
    }

    // add new shareholder(partner)
    function addShareHolder(address _shareHolderAddress, uint256 _partnerShare)
        public
        onlyDeveloper
    {
        require(!isShareHolder(_shareHolderAddress));
        require(totalAmountShares + _partnerShare <= 100);

        partners[_shareHolderAddress].share = _partnerShare;
        //set index for shareholder
        partners[_shareHolderAddress].index = shareHolderAddresses.push(_shareHolderAddress) - 1;

        // add share for total amount
        totalAmountShares += _partnerShare;

        createShareHolderEvent(ADDED_SHARE_HOLDER, _shareHolderAddress, _partnerShare);
    }

    // edit partner share
    function editShareHolder(address _shareHolderAddress, uint256 _partnerShare)
        public
        onlyDeveloper
    {
        require(isShareHolder(_shareHolderAddress));

        // update share percent
        uint256 updatedShareAmount = totalAmountShares - partners[_shareHolderAddress].share;
        updatedShareAmount += _partnerShare;

        require(updatedShareAmount <= 100);
        totalAmountShares = updatedShareAmount;

        partners[_shareHolderAddress].share = _partnerShare;

        createShareHolderEvent(EDITED_SHARE_HOLDER, _shareHolderAddress, _partnerShare);
    }

    // delete partner share
    function deleteShareHolder(address _shareHolderAddress)
        public
        onlyDeveloper
    {
        require(isShareHolder(_shareHolderAddress));

        // delete share percent
        totalAmountShares -= partners[_shareHolderAddress].share;

        uint256 rowToDelete = partners[_shareHolderAddress].index;
        address keyToMove = shareHolderAddresses[shareHolderAddresses.length - 1];

        shareHolderAddresses[rowToDelete] = keyToMove;
        partners[keyToMove].index = rowToDelete;

        shareHolderAddresses.length--;

        createShareHolderEvent(DELETED_SHARE_HOLDER, _shareHolderAddress, partners[_shareHolderAddress].share);
    }

    // get shareholder share by address
    function getHoldersShare(address _shareHolderAddress)
        public
        view
        returns(uint256 partnerShare)
    {
        require(isShareHolder(_shareHolderAddress));
        return(partners[_shareHolderAddress].share);
    }

    // get shareholder address at index
    function getShareHolderAtIndex(uint256 _index)
        public
        view
        returns(address shareHolderAddress)
    {
        return shareHolderAddresses[_index];
    }

    // get shareholder address and share amount at index
    function getShareHolderAddressAndShareAtIndex(uint256 _index)
        public
        view
        returns(address, uint256)
    {
        address shareHolderAddress = getShareHolderAtIndex(_index);
        return (shareHolderAddress, getHoldersShare(shareHolderAddress));
    }

    // get shareholders count
    function getShareHolderCount()
        public
        view
        returns(uint256 count)
    {
        return shareHolderAddresses.length;
    }

    // payable function for receiving customer funds
    function payDeveloper(${CUSTOM_SCAFFOLD_PARAMETERS}) public payable activated {
        require(msg.value == scaffoldAmount);
        payWithShares(msg.sender, msg.value, ${CUSTOM_RETURN_VARIABLES});
    }

    // transfer amount according shares
    function payWithShares(address _customerAddress, uint _transactionAmount, ${CUSTOM_SCAFFOLD_PARAMETERS}) internal {
        // platform fee
        uint256 platformFee = _transactionAmount.div(100).mul(3);
        // pay for shareholders and get developer amount
        uint256 developerAmount = payToShareHolders(_transactionAmount.sub(platformFee));

        // transfer amount for platform
        withdrawFunds(platformAddress, platformFee);
        // transfer amount for developer
        if(developerAmount > 0) {
            withdrawFunds(developerAddress, developerAmount);
        }

        generateTransaction(_customerAddress, developerAmount, ${CUSTOM_RETURN_VARIABLES});
    }

    // pay to shareholders according to their shares
    function payToShareHolders(uint256 _unpaidAmount) internal returns(uint256) {
        uint256 shareHolderIndexLength = getShareHolderCount();

        if(0 == shareHolderIndexLength) {
            return _unpaidAmount;
        }

        uint256 developerAmount = _unpaidAmount;
        for(uint256 row = 0; row < shareHolderIndexLength; row++) {
            address shareHolderAddress = getShareHolderAtIndex(row);
            uint256 shareHolderAmount = _unpaidAmount.div(100).mul(partners[shareHolderAddress].share);

            // reduce developer amount
            developerAmount = developerAmount.sub(shareHolderAmount);

            // transfer amount for shareholder
            withdrawFunds(shareHolderAddress, shareHolderAmount);

            PaidForShareHolder(
                PAID_FOR_SHARE_HOLDER,
                shareHolderAddress,
                shareHolderAmount
            );
        }

        return developerAmount;
    }

    function generateTransaction(
        address _customerAddress,
        uint256 _developerAmount,
        ${CUSTOM_SCAFFOLD_PARAMETERS}
    ) internal
    {
        // create transaction
        uint256 transactionIndex  = createScaffoldTransaction(_customerAddress, ${CUSTOM_RETURN_VARIABLES});

        PaymentCompleted(
            PAYMENT_COMPLETED,
            _customerAddress,
            _developerAmount,
            transactionIndex,
            ${CUSTOM_RETURN_VARIABLES}
        );
    }

    // create Scaffold transaction and add to array
    function createScaffoldTransaction(address _customerAddress, ${CUSTOM_SCAFFOLD_PARAMETERS})
        internal
        returns(uint256)
    {
        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
            customerAddress: _customerAddress,
            ${SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS}
        });

        openScaffoldTransactions.push(newTransaction);
        return ++scaffoldTransactionIndex;
    }

    function createShareHolderEvent(uint256 _actionType, address _shareHolderAddress, uint256 _share) internal {
        ShareHolderEvent(_actionType, _shareHolderAddress, _share);
    }

    // withdraw funds
    function withdrawFunds(address _to, uint256 _amount) private {
        _to.transfer(_amount);
        FundsDeposited(FUNDS_DEPOSITED, _amount, _to);
    }

    // check of the partner's address
    function isShareHolder(address _shareHolderAddress)
        private
        view
        returns(bool alreadyExists)
    {
        if(0 == getShareHolderCount()) return false;

        return (shareHolderAddresses[partners[_shareHolderAddress].index] == _shareHolderAddress);
    }

    // view current state of scaffold
    function getScaffoldSummary()
        public
        view
        returns (
            bytes32,
            bytes32,
            uint256,
            uint256,
            address,
            uint256
        )
    {
        return (
        fiatAmount,
        fiatCurrency,
        scaffoldAmount,
        scaffoldTransactionIndex,
        developerAddress,
        OPENToken.balanceOf(address(this))
        );
    }

}