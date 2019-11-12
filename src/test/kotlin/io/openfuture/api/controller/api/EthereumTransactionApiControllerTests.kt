package io.openfuture.api.controller.api

import io.openfuture.api.component.web3.event.ProcessorEventDecoder
import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.event.ActivatedScaffoldEvent
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumTransaction
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.EthereumScaffoldService
import io.openfuture.api.service.EthereumTransactionService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(EthereumTransactionApiController::class)
class EthereumTransactionApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: EthereumTransactionService

    @MockBean
    private lateinit var ethereumScaffoldService: EthereumScaffoldService

    @MockBean
    private lateinit var eventDecoder: ProcessorEventDecoder


    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val pageRequest = PageRequest()
        val transaction = EthereumTransaction("hash", "index", scaffold, "data", Date(1531128228590))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(ethereumScaffoldService.get(scaffold.address, openKey.user)).willReturn(scaffold)
        given(service.getAll(scaffold, pageRequest)).willReturn(PageImpl(listOf(transaction)))
        given(eventDecoder.getEvent(scaffold.address, transaction.data)).willReturn(ActivatedScaffoldEvent(true))

        mvc.perform(get("/api/ethereum-scaffolds/${scaffold.address}/transactions")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "totalCount": 1,
                      "list": [
                        ${expectTransactionJson(transaction)}
                      ]
                    }
                    """.trimIndent(), true))
    }

    private fun createScaffold(openKey: OpenKey) = EthereumScaffold("address", "abi", "2", USD.getId(), "0.00023", mutableListOf(), V1.getId(), openKey, "developerAddress", "description")


    private fun expectTransactionJson(transaction: EthereumTransaction) = """
                    {
                        "scaffold": {
                          "address": ${transaction.ethereumScaffold.address},
                          "user": {
                            "id": ${transaction.ethereumScaffold.openKey.user.id},
                            "credits": ${transaction.ethereumScaffold.openKey.user.credits},
                            "openKeys": [
                              {
                                "value": ${transaction.ethereumScaffold.openKey.value},
                                "enabled": ${transaction.ethereumScaffold.openKey.enabled},
                                "expiredDate": ${transaction.ethereumScaffold.openKey.expiredDate}
                              }
                            ],
                            "roles": [
                              {
                                "key": ${transaction.ethereumScaffold.openKey.user.roles.first().key}
                              }
                            ]
                          },
                          "abi": ${transaction.ethereumScaffold.abi},
                          "developerAddress": ${transaction.ethereumScaffold.developerAddress},
                          "description": ${transaction.ethereumScaffold.description},
                          "fiatAmount": "${transaction.ethereumScaffold.fiatAmount}",
                          "currency": ${transaction.ethereumScaffold.getCurrency().name},
                          "conversionAmount": "${transaction.ethereumScaffold.conversionAmount}",
                          "webHook": ${transaction.ethereumScaffold.webHook},
                          "properties": ${Arrays.toString(transaction.ethereumScaffold.property.toTypedArray())},
                          "version": ${transaction.ethereumScaffold.getVersion()}
                        },
                        "event":{"activated":true,"type":"ACTIVATED_SCAFFOLD"},
                        "date": "2018-07-09T09:23:48.590+0000"
                    }
                    """.trimIndent()

}
