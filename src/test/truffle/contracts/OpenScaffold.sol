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
//${SCAFFOLD_STRUCT_PROPERTIES};
}

// shareholder struct
struct Partner {
uint8 share;
uint index;
}


// events
    event PaymentCompleted(
    address customerAddress,
    uint transactionAmount,
    uint scaffoldTransactionIndex/*,
    ${CUSTOM_SCAFFOLD_PARAMETERS}*/
    );
    event FundsDeposited(uint _amount, address _toAddress);
    event ActivatedScaffold(bool activated);
    // shareholders events
    event AddedShareHolder(address userAddress, uint partnerShare);
    event EditedShareHolder(address userAddress, uint partnerShare);
    event DeletedShareHolder(address userAddress);
    event PayedForShareHolder(address userAddress, uint amount);


// custom dataTypes
// array for storage of transactions
OpenScaffoldTransaction[] public openScaffoldTransactions;
// array of shareholders addresses
address[] public shareHolderAddresses;
// mapping for storage of partners(shareholders)
mapping(address => Partner) public partners;

// constructor variables
address public  vendorAddress;
string public   scaffoldDescription;
string public   fiatAmount;
string public   fiatCurrency;
uint public     scaffoldAmount;
address private platformAddress;

// generated internally by contract
uint public scaffoldTransactionIndex;
address private scaffoldAddress = this;
// summarize all the shares that would not exceed the limit
uint8 private totalAmountShares = 0;

// OPEN token
uint constant private ACTIVATING_TOKENS_AMOUNT = 10 * 10**8;
address constant private OPEN_TOKEN_ADDRESS = 0x0/*${OPEN_TOKEN_ADDRESS}*/;
ERC20Token public OPENToken = ERC20Token(OPEN_TOKEN_ADDRESS);


// Throws if called by any account other than the vendor or OPEN platform addresses.
modifier onlyVendor() {
//require(msg.sender == vendorAddress || msg.sender == platformAddress);
_;
}

// Throws if contract is not activated.
modifier activated() {
require(OPENToken.balanceOf(address(this)) >= ACTIVATING_TOKENS_AMOUNT);
_;
}


function OpenScaffold(
address _vendorAddress,
address _platformAddress,
string _description,
string _fiatAmount,
string _fiatCurrency,
uint _scaffoldAmount
)
public
{
vendorAddress = _vendorAddress;
platformAddress = _platformAddress;
scaffoldDescription = _description;
fiatAmount = _fiatAmount;
fiatCurrency = _fiatCurrency;
scaffoldAmount = _scaffoldAmount;
}

// deactivate Scaffold contract by vendor
function deactivate() onlyVendor public activated {
OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));
ActivatedScaffold(false);
}

// get shareholder share by address
function getHoldersShare(address shareHolderAddress)
public
constant
returns(uint8 partnerShare) {
require(isShareHolder(shareHolderAddress));

return(partners[shareHolderAddress].share);
}

    // get shareholder address at index
    function getShareHolderAtIndex(uint index)
    public
    constant
    returns(address shareHolderAddress) {
        return shareHolderAddresses[index];
    }

    // get shareholder address and share amount at index
    function getShareHolderAddressAndShareAtIndex(uint index)
    public
    constant
    returns(address s1, uint8 s2) {
        address shareHolderAddress = getShareHolderAtIndex(index);
        uint8 share = getHoldersShare(shareHolderAddress);
        return (shareHolderAddress, share);
    }

// get shareholders count
function getShareHolderCount()
public
constant
returns(uint count) {
return shareHolderAddresses.length;
}

// add new shareholder
function addShareHolder(address shareHolderAddress, uint8 partnerShare)
public onlyVendor
returns(uint index) {
require(!isShareHolder(shareHolderAddress));
require(totalAmountShares + partnerShare <= 100);

partners[shareHolderAddress].share = partnerShare;
//.push() returns the new array length
partners[shareHolderAddress].index = shareHolderAddresses.push(shareHolderAddress) - 1;

// add share for summing
totalAmountShares += partnerShare;

AddedShareHolder(
shareHolderAddress,
partnerShare);

return getShareHolderCount();
}

// edit partner share
function editShareHolder(address shareHolderAddress, uint8 partnerShare)
public onlyVendor
returns(bool success) {
require(isShareHolder(shareHolderAddress));

// update share percent
uint8 updatedShareAmount = totalAmountShares - partners[shareHolderAddress].share;
updatedShareAmount += partnerShare;

require(updatedShareAmount <= 100);
totalAmountShares = updatedShareAmount;

partners[shareHolderAddress].share = partnerShare;

EditedShareHolder(
shareHolderAddress,
partnerShare);

return true;
}

// delete partner share
function deleteShareHolder(address shareHolderAddress)
public onlyVendor
returns(uint index) {
require(isShareHolder(shareHolderAddress));

// delete share percent
totalAmountShares -= partners[shareHolderAddress].share;

uint rowToDelete = partners[shareHolderAddress].index;
address keyToMove = shareHolderAddresses[shareHolderAddresses.length - 1];

shareHolderAddresses[rowToDelete] = keyToMove;
partners[keyToMove].index = rowToDelete;

shareHolderAddresses.length--;

DeletedShareHolder(shareHolderAddress);

return rowToDelete;
}

    // payable function for receiving customer funds
    function payVendor(/*${CUSTOM_SCAFFOLD_PARAMETERS}*/) public payable activated {
        require(msg.value == scaffoldAmount);
        payWithShares(msg.sender, msg.value);
    }

    // transfer amount according shares
    function payWithShares(address customerAddress, uint transactionAmount) internal {
        // platform fee
        uint256 platformFee = transactionAmount.div(100).mul(3);
        // unpaid amount
        uint256 unpaidAmount = transactionAmount.sub(platformFee);
        // vendor amount
        uint256 vendorAmount = payToShareHolders(unpaidAmount);

        // create transaction
        uint256 transactionIndex  = createScaffoldTransaction(customerAddress);

        // transfer amount for platform
        withdrawFunds(platformAddress, platformFee);
        // transfer amount for vendor
        if(vendorAmount > 0) {
            withdrawFunds(vendorAddress, vendorAmount);
        }

        PaymentCompleted(
        customerAddress,
        vendorAmount,
        transactionIndex/*,
            ${CUSTOM_RETURN_VARIABLES}*/
        );
    }

    // pay to shareholders according to their shares
    function payToShareHolders(uint256 unpaidAmount) internal returns(uint256) {
        uint256 shareHolderIndexLength = getShareHolderCount();

        if(0 == shareHolderIndexLength) {
            return unpaidAmount;
        }

        uint256 vendorAmount = unpaidAmount;
        for(uint8 row = 0; row < shareHolderIndexLength; row++) {
            address shareHolderAddress = getShareHolderAtIndex(row);
            uint256 shareHolderAmount = unpaidAmount.div(100).mul(partners[shareHolderAddress].share);

            // reduce vendor amount
            vendorAmount = vendorAmount.sub(shareHolderAmount);

            // transfer amount for shareholder
            //withdrawFunds(shareHolderAddress, shareHolderAmount);

            PayedForShareHolder(
            shareHolderAddress,
            shareHolderAmount);
        }

        return vendorAmount;
    }

    // generate Scaffold transaction and add to array
    function createScaffoldTransaction(address customerAddress) internal returns(uint) {
        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
        customerAddress: customerAddress/*,
            ${SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS}*/
        });

        openScaffoldTransactions.push(newTransaction);

        scaffoldTransactionIndex++;
        return scaffoldTransactionIndex;
    }

// withdraw funds
function withdrawFunds(address to, uint amount) private {
to.transfer(amount);
FundsDeposited(amount, to);
}

// check of the partner's address
function isShareHolder(address shareHolderAddress)
private
constant
returns(bool alreadyExists) {
if(shareHolderAddresses.length == 0) return false;

return (shareHolderAddresses[partners[shareHolderAddress].index] == shareHolderAddress);
}

// view current state of scaffold
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
