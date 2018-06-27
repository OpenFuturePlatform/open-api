package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.service.ScaffoldService
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

@WebMvcTest(ScaffoldApiController::class)
class ScaffoldApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: ScaffoldService


    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val pageRequest = PageRequest()

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getAll(openKey.user, pageRequest)).willReturn(PageImpl(listOf(scaffold)))

        mvc.perform(get("/api/scaffolds")
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

        mvc.perform(get("/api/scaffolds/" + scaffold.address)
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun compileTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val request = CompileScaffoldRequest("openKey", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)
        val compiledScaffoldDto = CompiledScaffoldDto("abi", "bin")

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.compile(request)).willReturn(compiledScaffoldDto)

        mvc.perform(post("/api/scaffolds/doCompile")
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
        val request = DeployScaffoldRequest("openKey", "developerAddress", "description",
                "2", USD, "0.0023", "webHook", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.deploy(request)).willReturn(scaffold)

        mvc.perform(post("/api/scaffolds/doDeploy")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun deployWhenUserWithoutDeployRoleShouldRedirectToIndexPageTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_INAPPROPRIATE")))
        val request = DeployScaffoldRequest("openKey", "developerAddress", "description",
                "2", USD, "0.0023", "webHook", listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(post("/api/scaffolds/doDeploy")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isForbidden)
    }

    @Test
    fun saveTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = SaveScaffoldRequest("address", "abi", "openKey", "developerAddress",
                "description", "2", USD, "0.0023", "webHook",
                listOf(createScaffoldPropertyDto()))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.save(request)).willReturn(scaffold)

        mvc.perform(post("/api/scaffolds")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun updateTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val scaffold = createScaffold(openKey)
        val request = UpdateScaffoldRequest("description")
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.update(scaffold.address, openKey.user, request)).willReturn(scaffold)

        mvc.perform(put("/api/scaffolds/" + scaffold.address)
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

        mvc.perform(patch("/api/scaffolds/" + scaffold.address)
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
        val scaffoldSummary = ScaffoldSummary(scaffold, ONE, ONE, true)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getScaffoldSummary(scaffoldAddress, openKey.user)).willReturn(scaffoldSummary)

        mvc.perform(get("/api/scaffolds/$scaffoldAddress/summary")
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

        mvc.perform(post("/api/scaffolds/$scaffoldAddress/doDeactivate")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)

        verify(service).deactivate(scaffoldAddress, openKey.user)
    }

    @Test
    fun getQuotaTest() {
        val scaffoldQuotaDto = ScaffoldQuotaDto(1, 10)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getQuota(openKey.user)).willReturn(scaffoldQuotaDto)

        mvc.perform(get("/api/scaffolds/quota")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json("""
                    {
                      "currentCount": ${scaffoldQuotaDto.currentCount},
                      "limitCount": ${scaffoldQuotaDto.limitCount}
                    }
                    """.trimIndent(), true))
    }

    @Test
    fun addShareHolderTest() {
        val address = "address"
        val request = AddShareHolderRequest(address, 3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(post("/api/scaffolds/$address/holders")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).addShareHolder(address, openKey.user, request)
    }

    @Test
    fun updateShareHolderTest() {
        val address = "address"
        val request = UpdateShareHolderRequest(address, 3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(put("/api/scaffolds/$address/holders")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).updateShareHolder(address, openKey.user, request)
    }

    @Test
    fun removeShareHolderTest() {
        val address = "address"
        val request = RemoveShareHolderRequest(address)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)

        mvc.perform(delete("/api/scaffolds/$address/holders")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).removeShareHolder(address, openKey.user, request)
    }

    private fun createScaffold(openKey: OpenKey) = Scaffold("address", openKey, "abi", "developerAddress",
            "description", "2", USD.getId(), "0.00023")

    private fun createScaffoldPropertyDto() = ScaffoldPropertyDto("name", PropertyType.STRING, "value")

    private fun expectScaffoldJson(scaffold: Scaffold) = """
                    {
                      "address": ${scaffold.address},
                      "user": {
                        "id": ${scaffold.openKey.user.id},
                        "credits": ${scaffold.openKey.user.credits},
                        "openKeys": [
                          {
                            "value": ${scaffold.openKey.value},
                            "enabled": ${scaffold.openKey.enabled},
                            "expiredDate": ${scaffold.openKey.expiredDate}
                          }
                        ],
                        "roles": [
                          {
                            "key": ${scaffold.openKey.user.roles.first().key}
                          }
                        ]
                      },
                      "abi": ${scaffold.abi},
                      "vendorAddress": ${scaffold.vendorAddress},
                      "description": ${scaffold.description},
                      "fiatAmount": "${scaffold.fiatAmount}",
                      "currency": ${scaffold.getCurrency().name},
                      "conversionAmount": "${scaffold.conversionAmount}",
                      "webHook": ${scaffold.webHook},
                      "properties": ${Arrays.toString(scaffold.property.toTypedArray())}
                    }
                    """.trimIndent()

    private fun expectScaffoldSummaryDtoJson(scaffoldSummary: ScaffoldSummary) = """
                    {
                      "scaffold": ${expectScaffoldJson(scaffoldSummary.scaffold)},
                      "transactionIndex": ${scaffoldSummary.transactionIndex},
                      "tokenBalance": ${scaffoldSummary.tokenBalance},
                      "enabled": ${scaffoldSummary.enabled},
                      "shareHolders": ${Arrays.toString(scaffoldSummary.shareHolders.toTypedArray())}
                    }
                    """.trimIndent()

    private fun createScaffoldSummary() = ScaffoldSummary(Scaffold("address", OpenKey(User("")), "", "", "", "", 1, ""),
            ZERO, ZERO, true)

}
