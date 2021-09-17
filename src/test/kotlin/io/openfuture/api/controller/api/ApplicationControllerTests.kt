package io.openfuture.api.controller.api

import io.openfuture.api.config.ControllerTests
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency
import org.junit.Test
import org.mockito.BDDMockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

@WebMvcTest(ApplicationApiController::class)
class ApplicationControllerTests: ControllerTests() {

    @Test
    fun getAllTest() {
        val openKey = createOpenKey(setOf(Role("ROLE_MASTER")))

        val user = createUser()
        val application = Application("Gateway 1",user, Currency.USD.getId(),457865,"https://openfuture.io/webhook",true)

        val pageRequest = PageRequest()

        BDDMockito.given(applicationService.getAll(user, pageRequest)).willReturn(PageImpl(listOf(application)))

        mvc.perform(
            MockMvcRequestBuilders.get("/api/application")
            .header(HttpHeaders.AUTHORIZATION, openKey.value))

            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json("""
                    {
                      "totalCount": 1,
                      "list": [
                        ${expectApplicationJson(application)}
                      ]
                    }
                    """.trimIndent(), true))
    }

    private fun createUser(): User = User("104113085667282103363", 0, Collections.emptySet(), Collections.emptySet())

    private fun expectApplicationJson(application: Application) = """
                    {
                        "name":${application.name},
                        "user": {
                            "id": ${application.user.id},
                            "credits": ${application.user.credits},
                            "openKeys": ${application.user.openKeys},
                            "roles": [
                              {
                                "key": ${application.user.roles.first().key}
                              }
                            ]
                          }
                        "webHook": ${application.webHook},
                        "defaultCurrency": ${application.getCurrency()?.name},
                        "expirationPeriod": ${application.expirationPeriod},
                        "active": ${application.active}
                      
                    }
                    """.trimIndent()
}