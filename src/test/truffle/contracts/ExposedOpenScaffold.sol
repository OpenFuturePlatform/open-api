pragma solidity ^0.4.0;
import "./OpenScaffold.sol";

contract ExposedOpenScaffold is OpenScaffold {

    function ExposedOpenScaffold(
        address _vendorAddress,
        address _platformAddress,
        string _description,
        bytes32 _fiatAmount,
        bytes32 _fiatCurrency,
        uint256 _scaffoldAmount
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
