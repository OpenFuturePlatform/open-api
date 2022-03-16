package io.openfuture.api.component.state

import io.openfuture.api.config.UnitTest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.state.*
import io.openfuture.api.entity.state.Blockchain
import io.openfuture.api.util.getOrderKey
import io.openfuture.api.util.getRandomNumber
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDateTime

class DefaultStateApiTests : UnitTest() {

    @Mock
    private lateinit var restTemplate: RestTemplate

    private lateinit var stateApi: StateApi

    @Before
    fun setUp() {
        stateApi = DefaultStateApi(restTemplate)
    }

    @Test
    fun createWalletTest() {
        val request = CreateStateWalletRequestMetadata(
            "webhook",
            listOf(KeyWalletDto("address", Blockchain.Ethereum.getValue())),
            WalletMetaData(
                "0",
                "1000",
                "op_UxQr1LLdREboF",
                "USD",
                "open",
                false
            )
        )
        val response = CreateStateWalletResponse(
            "webhook",
            "100",
            "op_UxQr1LLdREboF",
            BigDecimal.ZERO,
            listOf(WalletCreateResponse(Blockchain.Ethereum.getValue(),"address", BigDecimal.ZERO))
        )

        given(restTemplate.postForEntity("/wallets", request, CreateStateWalletResponse::class.java)).willReturn(ResponseEntity(response, HttpStatus.OK))

        val result = stateApi.createWallet("address", "webhook", Blockchain.Ethereum)

        Assertions.assertThat(result).isEqualTo(response)
    }

}
