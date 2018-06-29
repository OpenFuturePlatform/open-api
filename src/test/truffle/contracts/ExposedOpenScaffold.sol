pragma solidity ^0.4.0;
import "./OpenScaffold.sol";

contract ExposedOpenScaffold is OpenScaffold {

    function ExposedOpenScaffold(
    address _vendorAddress,
    address _platformAddress,
    string _description,
    string _fiatAmount,
    string _fiatCurrency,
    uint _scaffoldAmount
    ) OpenScaffold(
        _vendorAddress,
        _platformAddress,
        _description,
        _fiatAmount,
        _fiatCurrency,
        _scaffoldAmount)
    public {}

    function _createScaffoldTransaction(address customerAddress) public returns(uint){
        return createScaffoldTransaction(customerAddress);
    }

    function _payToShareHolders(uint256 unpaidAmount) public returns(uint) {
        return payToShareHolders(unpaidAmount);
    }

}
