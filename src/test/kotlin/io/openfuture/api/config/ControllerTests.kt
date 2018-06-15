package io.openfuture.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.annotation.PostConstruct

/**
 * @author Yauheni Efimenko
 */
@RunWith(SpringRunner::class)
@Import(SecurityConfig::class)
abstract class ControllerTests {

    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var clientRegictrationRepository: ClientRegistrationRepository

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    protected lateinit var keyService: OpenKeyService

    @MockBean
    private lateinit var properties: AuthorizationProperties

    protected val openKey = createOpenKey(setOf(Role("ROLE_DEPLOY")))


    companion object {
        const val DEFAULT_ID = 1L
        const val OPEN_TOKEN_VALUE = "open_token_value"
        const val AUTHORIZATION = "Authorization"
    }

    @PostConstruct
    private fun setUp() {
        given(keyService.find(OPEN_TOKEN_VALUE)).willReturn(openKey)
    }

    protected fun whenOpenTokenIsNotFoundShouldRedirectToIndexPage(url: String) {
        mvc.perform(get(url).header(AUTHORIZATION, "not_valid_token"))

                .andExpect(status().is3xxRedirection)
                .andExpect(redirectedUrl("http://localhost/"))
    }

    protected fun createOpenKey(roles: Set<Role>): OpenKey {
        val user = User("test", 0, mutableSetOf(), roles)
        val openKey = OpenKey(user, value = OPEN_TOKEN_VALUE)
        openKey.id = DEFAULT_ID
        user.id = DEFAULT_ID
        user.openKeys.add(openKey)

        return openKey
    }

}