package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.holder.AddShareHolderRequest
import io.openfuture.api.domain.holder.UpdateShareHolderRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Scaffold
import io.openfuture.api.entity.scaffold.ScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.ScaffoldService
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigInteger.ZERO

@WebMvcTest(ShareHolderApiController::class)
class ShareHolderApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: ScaffoldService


    @Test
    fun addShareHolderTest() {
        val address = "address"
        val request = AddShareHolderRequest(address, 3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.addShareHolder(address, openKey.user, request)).willReturn(createScaffoldSummary())

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
        val request = UpdateShareHolderRequest(3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.updateShareHolder(address, openKey.user, address, request)).willReturn(createScaffoldSummary())

        mvc.perform(put("/api/scaffolds/$address/holders/$address")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).updateShareHolder(address, openKey.user, address, request)
    }

    @Test
    fun removeShareHolderTest() {
        val address = "address"
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.removeShareHolder(address, openKey.user, address)).willReturn(createScaffoldSummary())

        mvc.perform(delete("/api/scaffolds/$address/holders/$address")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)

        verify(service).removeShareHolder(address, openKey.user, address)
    }

    private fun createScaffoldSummary() = ScaffoldSummary(Scaffold("address", OpenKey(User("")), "", "", "", "", 1, "",
            V1.getId()), ZERO, ZERO, true)

}