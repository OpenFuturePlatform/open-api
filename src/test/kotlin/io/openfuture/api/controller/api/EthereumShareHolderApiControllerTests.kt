package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.holder.AddEthereumShareHolderRequest
import io.openfuture.api.domain.holder.UpdateEthereumShareHolderRequest
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.EthereumScaffold
import io.openfuture.api.entity.scaffold.EthereumScaffoldSummary
import io.openfuture.api.entity.scaffold.ScaffoldVersion.V1
import io.openfuture.api.service.EthereumScaffoldService
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

@WebMvcTest(EthereumShareHolderApiController::class)
class EthereumShareHolderApiControllerTests : ControllerTests() {

    @MockBean
    private lateinit var service: EthereumScaffoldService


    @Test
    fun addShareHolderTest() {
        val address = "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5"
        val request = AddEthereumShareHolderRequest(address, 3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.addShareHolder(address, openKey.user, request)).willReturn(createScaffoldSummary())

        mvc.perform(post("/api/ethereum-scaffolds/$address/holders")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).addShareHolder(address, openKey.user, request)
    }

    @Test
    fun updateShareHolderTest() {
        val address = "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5"
        val request = UpdateEthereumShareHolderRequest(3)
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))
        val requestJson = objectMapper.writeValueAsString(request)

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.updateShareHolder(address, openKey.user, address, request)).willReturn(createScaffoldSummary())

        mvc.perform(put("/api/ethereum-scaffolds/$address/holders/$address")
                .header(AUTHORIZATION, openKey.value)
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))

                .andExpect(status().isOk)

        verify(service).updateShareHolder(address, openKey.user, address, request)
    }

    @Test
    fun removeShareHolderTest() {
        val address = "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5"
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        given(keyService.find(openKey.value)).willReturn(openKey)
        given(service.removeShareHolder(address, openKey.user, address)).willReturn(createScaffoldSummary())

        mvc.perform(delete("/api/ethereum-scaffolds/$address/holders/$address")
                .header(AUTHORIZATION, openKey.value))

                .andExpect(status().isOk)

        verify(service).removeShareHolder(address, openKey.user, address)
    }

    private fun createScaffoldSummary() = EthereumScaffoldSummary(EthereumScaffold(
            "", "", "", 1, "", mutableListOf(), V1.getId(), OpenKey(User("")), "0x8cf1664B09F216538bc9A32B2c26f85a19fd76B5", ""), ZERO, ZERO, true)

}