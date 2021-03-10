package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.EthereumScaffoldService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import java.util.*

@WebMvcTest(EthereumScaffoldApiController::class)
class EthereumScaffoldApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: EthereumScaffoldService


    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val pageRequest = PageRequest()

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getAll(openKey.user, pageRequest)).willReturn(PageImpl(listOf(scaffold)))

        mvc.perform(get("/api/ethereum-scaffolds")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "totalCount": 1,
                      "list": [
                        ${expectScaffoldJson(scaffold)}
                      ]
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun getTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.get(scaffold.address, openKey.user)).willReturn(scaffold)

        mvc.perform(get("/api/ethereum-scaffolds/" + scaffold.address)
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun compileTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val request = CompileEthereumScaffoldRequest("openKey", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)
        val compiledScaffoldDto = CompiledScaffoldDto("abi", "bin")

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.compile(request)).willReturn(compiledScaffoldDto)

        mvc.perform(post("/api/ethereum-scaffolds/doCompile")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "abi": ${compiledScaffoldDto.abi},
                      "bin": ${compiledScaffoldDto.bin}
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun deployTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = DeployEthereumScaffoldRequest("openKey", "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "description",
                "2", USD, "0.0023", "webHook", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.deploy(request)).willReturn(scaffold)

        mvc.perform(post("/api/ethereum-scaffolds/doDeploy")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun deployWhenUserWithoutDeployRoleShouldRedirectToIndexPageTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_INAPPROPRIATE")))
        val request = DeployEthereumScaffoldRequest("openKey", "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "description",
                "2", USD, "0.0023", "webHook", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(post("/api/ethereum-scaffolds/doDeploy")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isForbidden)
    }

    @Test
    fun saveTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = SaveEthereumScaffoldRequest("0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "abi", "openKey",
                "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "description", "2", USD, "0.0023", "https://www.openfuture.io/api",
                listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.save(request)).willReturn(scaffold)

        mvc.perform(post("/api/ethereum-scaffolds")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun saveShouldReturnBadRequestTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val request = SaveEthereumScaffoldRequest("0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "abi", "openKey",
                "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", "description", "2", USD, "0.0023", "invalid webhhok",
                listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(post("/api/ethereum-scaffolds")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isBadRequest)
    }

    @Test
    fun updateTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = UpdateEthereumScaffoldRequest("description")
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.update(scaffold.address, openKey.user, request)).willReturn(scaffold)

        mvc.perform(put("/api/ethereum-scaffolds/" + scaffold.address)
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun setWebHookTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = SetWebHookRequest("https://test.com/test")
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.setWebHook(scaffold.address, request, openKey.user)).willReturn(scaffold)

        mvc.perform(patch("/api/ethereum-scaffolds/" + scaffold.address)
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun getScaffoldSummaryTest() {
        val scaffoldAddress = "address"
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val scaffoldSummary = EthereumScaffoldSummary(scaffold, ONE, ONE, true)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getScaffoldSummary(scaffoldAddress, openKey.user)).willReturn(scaffoldSummary)

        mvc.perform(get("/api/ethereum-scaffolds/$scaffoldAddress/summary")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldSummaryDtoJson(scaffoldSummary), true))
    }

    @Test
    fun deactivateTest() {
        val scaffoldAddress = "address"
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.deactivate(scaffoldAddress, openKey.user)).willReturn(createScaffoldSummary())

        mvc.perform(delete("/api/ethereum-scaffolds/$scaffoldAddress")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)

        verify(service).deactivate(scaffoldAddress, openKey.user)
    }

    @Test
    fun activateTest() {
        val scaffoldAddress = "address"
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.activate(scaffoldAddress, openKey.user)).willReturn(createScaffoldSummary())

        mvc.perform(post("/api/ethereum-scaffolds/$scaffoldAddress")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)

        verify(service).activate(scaffoldAddress, openKey.user)
    }

    @Test
    fun getQuotaTest() {
        val scaffoldQuotaDto = EthereumScaffoldQuotaDto(1, 10)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getQuota(openKey.user)).willReturn(scaffoldQuotaDto)

        mvc.perform(get("/api/ethereum-scaffolds/quota")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "currentCount": ${scaffoldQuotaDto.currentCount},
                      "limitCount": ${scaffoldQuotaDto.limitCount}
                    }
                    """.trimIndent(), true))
    }

    private fun createScaffold(openKey: OpenKey) = EthereumScaffold("address","abi", "2", USD.getId(), "0.00023", mutableListOf(), V1.getId(), openKey, "developerAddress", "description")

    private fun createScaffoldPropertyDto() = EthereumScaffoldPropertyDto("name", PropertyType.STRING, "value")

    private fun expectScaffoldJson(ethereumScaffold: EthereumScaffold) = """
                    {
                      "address": ${ethereumScaffold.address},
                      "user": {
                        "id": ${ethereumScaffold.openKey.user.id},
                        "credits": ${ethereumScaffold.openKey.user.credits},
                        "openKeys": [
                          {
                            "value": ${ethereumScaffold.openKey.value},
                            "enabled": ${ethereumScaffold.openKey.enabled},
                            "expiredDate": ${ethereumScaffold.openKey.expiredDate}
                          }
                        ],
                        "roles": [
                          {
                            "key": ${ethereumScaffold.openKey.user.roles.first().key}
                          }
                        ]
                      },
                      "abi": ${ethereumScaffold.abi},
                      "developerAddress": ${ethereumScaffold.developerAddress},
                      "description": ${ethereumScaffold.description},
                      "fiatAmount": "${ethereumScaffold.fiatAmount}",
                      "currency": ${ethereumScaffold.getCurrency().name},
                      "conversionAmount": "${ethereumScaffold.conversionAmount}",
                      "webHook": ${ethereumScaffold.webHook},
                      "properties": ${Arrays.toString(ethereumScaffold.property.toTypedArray())},
                      "version": ${ethereumScaffold.getVersion()}
                    }
                    """.trimIndent()

    private fun expectScaffoldSummaryDtoJson(ethereumScaffoldSummary: EthereumScaffoldSummary) = """
                    {
                      "scaffold": ${expectScaffoldJson(ethereumScaffoldSummary.ethereumScaffold)},
                      "transactionIndex": ${ethereumScaffoldSummary.transactionIndex},
                      "tokenBalance": ${ethereumScaffoldSummary.tokenBalance},
                      "enabled": ${ethereumScaffoldSummary.enabled},
                      "shareHolders": ${Arrays.toString(ethereumScaffoldSummary.ethereumShareHolders.toTypedArray())}
                    }
                    """.trimIndent()

    private fun createScaffoldSummary() = EthereumScaffoldSummary(EthereumScaffold("address", "", "", 1, "", mutableListOf(), V1.getId(), OpenKey(User("")), "", ""), ZERO, ZERO, true)

}
