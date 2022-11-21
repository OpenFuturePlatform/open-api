package io.openfuture.api.component.state

import io.openfuture.api.config.UnitTest
import io.openfuture.api.domain.key.KeyWalletDto
import io.openfuture.api.domain.state.*
import io.openfuture.api.entity.state.Blockchain
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
import java.util.*

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
        val request = CreateStateWalletRequest(
            "address",
            "webhook",
            Blockchain.Ethereum.getValue(),
            "applicationId"
        )
        val response = StateWalletDto(
            UUID.randomUUID().toString(), "address", "webhook", Blockchain.Ethereum, LocalDateTime.now())

        given(restTemplate.postForEntity("/wallets/single", request, StateWalletDto::class.java)).willReturn(ResponseEntity(response, HttpStatus.OK))

        val result = stateApi.createWallet("address", "webhook", Blockchain.Ethereum, "applicationId")

        Assertions.assertThat(result).isEqualTo(response)
    }

}
