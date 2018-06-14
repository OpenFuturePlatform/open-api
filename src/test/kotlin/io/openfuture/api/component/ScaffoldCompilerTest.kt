package io.openfuture.api.component

import io.openfuture.api.config.propety.EthereumProperties
import io.openfuture.api.domain.scaffold.ScaffoldPropertyDto
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.exception.CompileException
import io.openfuture.api.ADDRESS_VALUE
import io.openfuture.api.UnitTest
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
                "pragma solidity ^0.4.19;\n" +
                "\n" +
                "/**\n" +
                " * @title SafeMath\n" +
                " * @dev Math operations with safety checks that throw on error\n" +
                " */\n" +
                "\n" +
                "library SafeMath {\n" +
                "\n" +
                "    function mul(uint256 a, uint256 b) internal constant returns (uint256) {\n" +
                "        uint256 c = a * b;\n" +
                "        assert(a == 0 || c / a == b);\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "    function div(uint256 a, uint256 b) internal constant returns (uint256) {\n" +
                "        // assert(b > 0); // Solidity automatically throws when dividing by 0\n" +
                "        uint256 c = a / b;\n" +
                "        // assert(a == b * c + a % b); // There is no case in which this doesn't hold\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "    function sub(uint256 a, uint256 b) internal constant returns (uint256) {\n" +
                "        assert(b <= a);\n" +
                "        return a - b;\n" +
                "    }\n" +
                "\n" +
                "    function add(uint256 a, uint256 b) internal constant returns (uint256) {\n" +
                "        uint256 c = a + b;\n" +
                "        assert(c >= a);\n" +
                "        return c;\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "/**\n" +
                " * @title ERC20Basic\n" +
                " * @dev Simpler version of ERC20 interface\n" +
                " */\n" +
                "contract ERC20Token {\n" +
                "  function balanceOf(address who) public constant returns (uint256);\n" +
                "  function transfer(address to, uint256 value) public returns (bool);\n" +
                "}\n" +
                "\n" +
                "contract OpenScaffold {\n" +
                "\n" +
                "    using SafeMath for uint256;\n" +
                "\n" +
                "    // on-chain transaction storage\n" +
                "    struct OpenScaffoldTransaction {\n" +
                "        address customerAddress;\n" +
                "        string dfsf;\n" +
                "    }\n" +
                "\n" +
                "    // events\n" +
                "    event paymentComplete(\n" +
                "        address customerAddress,\n" +
                "        uint transactionAmount,\n" +
                "        uint scaffoldTransactionIndex,\n" +
                "        string dfsf\n" +
                "        );\n" +
                "    event fundsDeposited(uint _amount, address _toAddress);\n" +
                "    event activatedScaffold(bool activated);\n" +
                "\n" +
                "    // custom dataTypes - array for storage of transactions\n" +
                "    OpenScaffoldTransaction[] public openScaffoldTransactions;\n" +
                "\n" +
                "    // constructor variables\n" +
                "    address public  vendorAddress;\n" +
                "    address private developerAddress;\n" +
                "    string public   scaffoldDescription;\n" +
                "    string public   fiatAmount;\n" +
                "    string          fiatCurrency;\n" +
                "    uint public     scaffoldAmount;\n" +
                "\n" +
                "    // generated internally by contract\n" +
                "    uint public scaffoldTransactionIndex;\n" +
                "    address private scaffoldAddress = this;\n" +
                "\n" +
                "    // OPEN token\n" +
                "    uint constant private ACTIVATING_TOKENS_AMOUNT = 10 * 10**8;\n" +
                "    address constant private OPEN_TOKEN_ADDRESS = 0xD57B27f6ebA186D56ec2AaaF9BbB438678DFd4f1;\n" +
                "    ERC20Token public OPENToken = ERC20Token(OPEN_TOKEN_ADDRESS);\n" +
                "\n" +
                "\n" +
                "    // Throws if called by any account other than the developer.\n" +
                "    modifier onlyVendor() {\n" +
                "        require(msg.sender == developerAddress);\n" +
                "        _;\n" +
                "    }\n" +
                "\n" +
                "    // Throws if contract is not activated.\n" +
                "    modifier activated() {\n" +
                "        require(OPENToken.balanceOf(address(this)) >= ACTIVATING_TOKENS_AMOUNT);\n" +
                "        _;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    function OpenScaffold(\n" +
                "        address _vendorAddress,\n" +
                "        address _developerAddress,\n" +
                "        string _description,\n" +
                "        string _fiatAmount,\n" +
                "        string _fiatCurrency,\n" +
                "        uint _scaffoldAmount\n" +
                "    )\n" +
                "        public\n" +
                "    {\n" +
                "        vendorAddress = _vendorAddress;\n" +
                "        developerAddress = _developerAddress;\n" +
                "        scaffoldDescription = _description;\n" +
                "        fiatAmount = _fiatAmount;\n" +
                "        fiatCurrency = _fiatCurrency;\n" +
                "        scaffoldAmount = _scaffoldAmount;\n" +
                "    }\n" +
                "\n" +
                "    // deactivate Scaffold contract by vendor\n" +
                "    function deactivate() onlyVendor public activated {\n" +
                "        OPENToken.transfer(vendorAddress, OPENToken.balanceOf(address(this)));\n" +
                "        activatedScaffold(false);\n" +
                "    }\n" +
                "\n" +
                "    function payVendor(string dfsf) public payable activated {\n" +
                "        require(msg.value == scaffoldAmount);\n" +
                "        scaffoldTransactionIndex++;\n" +
                "\n" +
                "        address customerAddress = msg.sender;\n" +
                "        uint256 transactionAmount = msg.value;\n" +
                "        // developer fee\n" +
                "        uint256 developerFee = transactionAmount.div(100).mul(3);\n" +
                "        uint256 vendorAmount = transactionAmount.sub(developerFee);\n" +
                "\n" +
                "        OpenScaffoldTransaction memory newTransaction = OpenScaffoldTransaction({\n" +
                "            customerAddress: customerAddress,\n" +
                "            dfsf: dfsf\n" +
                "            });\n" +
                "\n" +
                "        openScaffoldTransactions.push(newTransaction);\n" +
                "\n" +
                "        // transfer amount\n" +
                "        withdrawFunds(vendorAddress, vendorAmount);\n" +
                "        withdrawFunds(developerAddress, developerFee);\n" +
                "\n" +
                "        paymentComplete(\n" +
                "            customerAddress,\n" +
                "            vendorAmount,\n" +
                "            scaffoldTransactionIndex,\n" +
                "            dfsf\n" +
                "            );\n" +
                "    }\n" +
                "\n" +
                "    function withdrawFunds(address to, uint amount) private {\n" +
                "        to.transfer(amount);\n" +
                "        fundsDeposited(amount, to);\n" +
                "    }\n" +
                "\n" +
                "    function getScaffoldSummary() public view returns (string, string, string, uint, uint, address, uint) {\n" +
                "        return (\n" +
                "          scaffoldDescription,\n" +
                "          fiatAmount,\n" +
                "          fiatCurrency,\n" +
                "          scaffoldAmount,\n" +
                "          scaffoldTransactionIndex,\n" +
                "          vendorAddress,\n" +
                "          OPENToken.balanceOf(address(this))\n" +
                "        );\n" +
                "    }\n" +
                "\n" +
                "}")

        scaffoldCompiler.compile(listOf(scaffoldPropertyDto))
    }

    @Test(expected = CompileException::class)
    fun compileWithIncorrectScaffold() {
        val scaffoldPropertyDto = ScaffoldPropertyDto("SCAFFOLD_STRUCT_PROPERTIES", PropertyType.STRING, "value")

        given(properties.openTokenAddress).willReturn(ADDRESS_VALUE)
        given(templateProcessor.getContent(anyString(), anyMap())).willReturn(
                "    function getScaffoldSummary() public view returns (string, string, string, uint, uint, address, uint) {\n" +
                "        return (\n" +
                "          scaffoldDescription,\n" +
                "          fiatAmount,\n" +
                "          fiatCurrency,\n" +
                "          scaffoldAmount,\n" +
                "          scaffoldTransactionIndex,\n" +
                "          vendorAddress,\n" +
                "          OPENToken.balanceOf(address(this))\n" +
                "        );\n" +
                "    }\n" +
                "\n" +
                "}")

        scaffoldCompiler.compile(listOf(scaffoldPropertyDto))
    }

}