package io.openfuture.api.component.scaffold

import io.openfuture.api.component.scaffold.compiler.EthereumScaffoldCompiler
import io.openfuture.api.component.scaffold.compiler.V1EthereumScaffoldCompiler
import io.openfuture.api.component.solidity.CompilationResult
import io.openfuture.api.component.solidity.SolidityCompiler
import io.openfuture.api.component.template.TemplateProcessor
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.EthereumScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.PropertyType.STRING
import io.openfuture.api.exception.CompileException
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import java.nio.charset.Charset

internal class V1ScaffoldCompilerTests : UnitTest() {

    @Mock private lateinit var templateProcessor: TemplateProcessor
    @Mock private lateinit var properties: EthereumProperties
    @Mock private lateinit var solidityCompiler: SolidityCompiler
    @Mock private lateinit var compilationResult: CompilationResult

    private lateinit var ethereumScaffoldCompiler: EthereumScaffoldCompiler


    @Before
    fun setUp() {
        ethereumScaffoldCompiler = V1EthereumScaffoldCompiler(templateProcessor, properties, solidityCompiler, compilationResult)
    }

    @Test
    fun compileTest() {
        val parameters = createParameters()
        val scaffoldContent = createScaffoldContent()

        given(properties.openTokenAddress).willReturn("0xba37163625b3f2e96112562858c12b75963af138")
        given(templateProcessor.getContent(scaffoldContent, parameters)).willReturn(
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

        ethereumScaffoldCompiler.compile(createScaffoldPropertyDtos())
    }

    @Test(expected = CompileException::class)
    fun compileWhenIncorrectScaffoldShouldThrowExceptionTest() {
        val parameters = createParameters()
        val scaffoldContent = createScaffoldContent()

        given(properties.openTokenAddress).willReturn("0xba37163625b3f2e96112562858c12b75963af138")
        given(templateProcessor.getContent(scaffoldContent, parameters)).willReturn(
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

        ethereumScaffoldCompiler.compile(createScaffoldPropertyDtos())
    }

    private fun createScaffoldPropertyDtos(): List<EthereumScaffoldPropertyDto> = listOf(EthereumScaffoldPropertyDto("value", STRING, "defaultValue"))

    private fun createParameters(): Map<String, String> {
        val params = HashMap<String, String>()
        params["SCAFFOLD_STRUCT_PROPERTIES"] = "bytes32 value;"
        params["CUSTOM_SCAFFOLD_PARAMETERS"] = "bytes32 value"
        params["SCAFFOLD_STRUCT_TRANSACTION_ARGUMENTS"] = "value: value"
        params["CUSTOM_RETURN_VARIABLES"] = "value"
        params["OPEN_TOKEN_ADDRESS"] = "0xba37163625b3f2e96112562858c12b75963af138"

        return params
    }

    private fun createScaffoldContent(): String {
        val resource = javaClass.classLoader
                .getResource("templates/ethereum/scaffold_${ethereumScaffoldCompiler.getVersion().name.toLowerCase()}.ftl")
        return IOUtils.toString(resource, Charset.defaultCharset())
    }

}