package io.openfuture.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.entity.auth.OpenKey
import io.openfuture.api.entity.auth.Role
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import io.openfuture.api.service.OpenKeyService
import io.openfuture.api.service.UserService
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc

@RunWith(SpringRunner::class)
@Import(SecurityConfig::class)
abstract class ControllerTests {

    @Autowired
    protected lateinit var mvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var clientRegistrationRepository: ClientRegistrationRepository

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    protected lateinit var keyService: OpenKeyService

    @MockBean
    protected lateinit var applicationService: ApplicationService

    @MockBean
    private lateinit var properties: AuthorizationProperties


    protected fun createOpenKey(roles: Set<Role>): OpenKey {
        val user = User("test", 0, mutableSetOf(), roles)
        val openKey = OpenKey(user, value = "open_token_value")
        openKey.id = 1
        user.id = 1
        user.openKeys.add(openKey)

        return openKey
    }

}