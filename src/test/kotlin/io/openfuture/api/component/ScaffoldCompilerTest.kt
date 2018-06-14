package io.openfuture.api.component

import io.openfuture.api.ADDRESS_VALUE
import io.openfuture.api.UnitTest
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.exception.CompileException
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock

/**
 * @author Alexey Skadorva
 */
internal class ScaffoldCompilerTest : UnitTest() {

    @Mock private lateinit var templateProcessor: TemplateProcessor
    @Mock private lateinit var properties: EthereumProperties

    private lateinit var scaffoldCompiler: ScaffoldCompiler


    @Before
    fun setUp() {
        scaffoldCompiler = ScaffoldCompiler(templateProcessor, properties)
    }

    @Test
    fun compile() {
        val scaffoldPropertyDto = ScaffoldPropertyDto("SCAFFOLD_STRUCT_PROPERTIES", PropertyType.STRING, "value")

        given(properties.openTokenAddress).willReturn(ADDRESS_VALUE)
        given(templateProcessor.getContent(ArgumentMatchers.anyString(), ArgumentMatchers.anyMap())).willReturn(
                """pragma solidity ^0.4.19;

                library SafeMath {

                    function mul(uint256 a, uint256 b) internal constant returns (uint256) {
                        uint256 c = a * b;
                        assert(a == 0 || c / a == b);
                        return c;
                    }

                    function div(uint256 a, uint256 b) internal constant returns (uint256) {
                        uint256 c = a / b;
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

                contract ERC20Token {
                  function balanceOf(address who) public constant returns (uint256);
                  function transfer(address to, uint256 value) public returns (bool);
                }

                contract OpenScaffold {

                    using SafeMath for uint256;

                    struct OpenScaffoldTransaction {
                        address customerAddress;
                        string dfsf;
                    }

                    event paymentComplete(
                        address customerAddress,
                        uint transactionAmount,
                        uint scaffoldTransactionIndex,
                        string dfsf
                        );
                    event fundsDeposited(uint _amount, address _toAddress);
                    event activatedScaffold(bool activated);

                    OpenScaffoldTransaction[] public openScaffoldTransactions;

                    address public  vendorAddress;
                    address private developerAddress;
                    string public   scaffoldDescription;
                    string public   fiatAmount;
                    string          fiatCurrency;
                    uint public     scaffoldAmount;

                    uint public scaffoldTransactionIndex;
                    address private scaffoldAddress = this;

                    uint constant private ACTIVATING_TOKENS_AMOUNT = 10 * 10**8;
                    address constant private OPEN_TOKEN_ADDRESS = 0xD57B27f6ebA186D56ec2AaaF9BbB438678DFd4f1;
                    ERC20Token public OPENToken = ERC20Token(OPEN_TOKEN_ADDRESS);

                    modifier onlyVendor() {
                        require(msg.sender == developerAddress);
                        _;
                    }

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

                    function deactivate() onlyVendor public activated {
                        OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));
                        activatedScaffold(false);
                    }

                    function payVendor(string dfsf) public payable activated {
                        require(msg.value == scaffoldAmount);
                        scaffoldTransactionIndex;

                        address customerAddress = msg.sender;
                        uint256 transactionAmount = msg.value;
                        uint256 developerFee = transactionAmount.div(100).mul(3);
                        uint256 vendorAmount = transactionAmount.sub(developerFee);

                        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({
                            customerAddress: customerAddress,
                            dfsf: dfsf
                            });

                        openScaffoldTransactions.push(newTransaction);

                        withdrawFunds(vendorAddress, vendorAmount);
                        withdrawFunds(developerAddress, developerFee);

                        paymentComplete(
                            customerAddress,
                            vendorAmount,
                            scaffoldTransactionIndex,
                            dfsf
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
                }""")

        scaffoldCompiler.compile(listOf(scaffoldPropertyDto))
    }

    @Test(expected = CompileException::class)
    fun compileWithIncorrectScaffold() {
        val scaffoldPropertyDto = ScaffoldPropertyDto("SCAFFOLD_STRUCT_PROPERTIES", PropertyType.STRING, "value")

        given(properties.openTokenAddress).willReturn(ADDRESS_VALUE)
        given(templateProcessor.getContent(anyString(), anyMap())).willReturn(
                """function getScaffoldSummary() public view returns (string, string, string, uint, uint, address, uint) {
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

                }""")

        scaffoldCompiler.compile(listOf(scaffoldPropertyDto))
    }

}