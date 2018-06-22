package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.Transaction
import io.openfuture.api.service.ScaffoldService
import io.openfuture.api.service.TransactionService
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

@WebMvcTest(TransactionApiController::class)
class TransactionApiControllerTest : ControllerTests() {

    @MockBean
    private lateinit var service: TransactionService

    @MockBean
    private lateinit var scaffoldService: ScaffoldService


    @Test
    fun getAll() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val scaffold = createScaffold(openKey)
        val pageRequest = PageRequest()
        val transaction = Transaction(scaffold, "data", "type")

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(scaffoldService.get(scaffold.address, openKey.user)).willReturn(scaffold)
        given(service.getAll(scaffold, pageRequest)).willReturn(PageImpl(listOf(transaction)))

        mvc.perform(get("/api/scaffolds/${scaffold.address}/transactions")
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

    private fun createScaffold(openKey: OpenKey) = Scaffold("address", openKey, "abi", "developerAddress",
            "description", "2", USD.getId(), "0.00023")


    private fun expectTransactionJson(transaction: Transaction) = """
                    {
                        "scaffold": {
                          "address": ${transaction.scaffold.address},
                          "user": {
                            "id": ${transaction.scaffold.openKey.user.id},
                            "credits": ${transaction.scaffold.openKey.user.credits},
                            "openKeys": [
                              {
                                "value": ${transaction.scaffold.openKey.value},
                                "enabled": ${transaction.scaffold.openKey.enabled},
                                "expiredDate": ${transaction.scaffold.openKey.expiredDate}
                              }
                            ],
                            "roles": [
                              {
                                "key": ${transaction.scaffold.openKey.user.roles.first().key}
                              }
                            ]
                          },
                          "abi": ${transaction.scaffold.abi},
                          "developerAddress": ${transaction.scaffold.developerAddress},
                          "description": ${transaction.scaffold.description},
                          "fiatAmount": "${transaction.scaffold.fiatAmount}",
                          "currency": ${transaction.scaffold.getCurrency().name},
                          "conversionAmount": "${transaction.scaffold.conversionAmount}",
                          "properties": ${Arrays.toString(transaction.scaffold.property.toTypedArray())},
                          "enabled": ${transaction.scaffold.enabled}
                        },
                        "data": ${transaction.data},
                        "type": ${transaction.type}
                    }
                    """.trimIndent()

}
