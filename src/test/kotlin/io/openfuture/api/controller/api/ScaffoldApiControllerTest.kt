package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.scaffold.*
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.scaffold.Currency.USD
import io.openfuture.api.entity.scaffold.PropertyType
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.service.ScaffoldService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal.ONE
import java.math.BigInteger.TEN
import java.util.*

/**
 * @author Yauheni Efimenko
 */
@WebMvcTest(ScaffoldApiController::class)
class ScaffoldApiControllerTest : ControllerTests() {

    @MockBean
    private lateinit var service: ScaffoldService


    @Test
    fun getAll() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
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
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val scaffold = createScaffold(openKey)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.get(scaffold.address, openKey.user)).willReturn(scaffold)

        mvc.perform(get("/api/scaffolds/" + scaffold.address)
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldJson(scaffold), true))
    }

    @Test
    fun compile() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
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
    fun deploy() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
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
    fun deployWhenUserWithoutDeployRoleShouldRedirectToIndexPage() {
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
    fun save() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
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
    fun setWebHook() {
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))
        val scaffold = createScaffold(openKey)
        val request = SetWebHookRequest("https://test.com")
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
    fun getScaffoldSummary() {
        val scaffoldAddress = "address"
        val scaffoldSummaryDto = ScaffoldSummaryDto("abi", "description", "2", USD.name,
                ONE, TEN, "vendorAddress", TEN, true)
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.getScaffoldSummary(scaffoldAddress, openKey.user)).willReturn(scaffoldSummaryDto)

        mvc.perform(get("/api/scaffolds/$scaffoldAddress/summary")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldSummaryDtoJson(scaffoldSummaryDto), true))
    }

    @Test
    fun deactivate() {
        val scaffoldAddress = "address"
        val scaffoldSummaryDto = ScaffoldSummaryDto("abi", "description", "2", USD.name,
                ONE, TEN, "vendorAddress", TEN, true)
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.deactivate(scaffoldAddress, openKey.user)).willReturn(scaffoldSummaryDto)

        mvc.perform(post("/api/scaffolds/$scaffoldAddress/doDeactivate")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)
                .andExpect(content().json(expectScaffoldSummaryDtoJson(scaffoldSummaryDto), true))
    }

    @Test
    fun getQuota() {
        val scaffoldQuotaDto = ScaffoldQuotaDto(1, 10)
        val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))

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
                      "developerAddress": ${scaffold.developerAddress},
                      "description": ${scaffold.description},
                      "fiatAmount": "${scaffold.fiatAmount}",
                      "currency": ${scaffold.getCurrency().name},
                      "conversionAmount": "${scaffold.conversionAmount}",
                      "properties": ${Arrays.toString(scaffold.property.toTypedArray())},
                      "enabled": ${scaffold.enabled}
                    }
                    """.trimIndent()

    private fun expectScaffoldSummaryDtoJson(scaffoldSummaryDto: ScaffoldSummaryDto) = """
                    {
                      "abi": ${scaffoldSummaryDto.abi},
                      "description": ${scaffoldSummaryDto.description},
                      "fiatAmount": "${scaffoldSummaryDto.fiatAmount}",
                      "fiatCurrency": ${scaffoldSummaryDto.fiatCurrency},
                      "amount": ${scaffoldSummaryDto.amount},
                      "transactionIndex": ${scaffoldSummaryDto.transactionIndex},
                      "vendorAddress": ${scaffoldSummaryDto.vendorAddress},
                      "tokenBalance": ${scaffoldSummaryDto.tokenBalance},
                      "enabled": ${scaffoldSummaryDto.enabled}
                    }
                    """.trimIndent()

}
