package io.openfuture.api.service

import io.openfuture.api.component.key.KeyApi
import io.openfuture.api.component.state.StateApi
import io.openfuture.api.config.UnitTest
import io.openfuture.api.config.any
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.entity.scaffold.Currency
import io.openfuture.api.repository.ApplicationRepository
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mock
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class DefaultApplicationTests : UnitTest() {

    @Mock
    private lateinit var pageable: Pageable

    @Mock
    private lateinit var repository: ApplicationRepository

    @Mock
    private lateinit var keyApi: KeyApi

    @Mock
    private lateinit var stateApi: StateApi

    private lateinit var service: ApplicationService

    @Before
    fun setUp() {
        service = DefaultApplicationService(repository,keyApi, stateApi)
    }

    @Test
    fun getAllTest() {
        val user = createUser()
        val expectedApplicationPages = PageImpl(Collections.singletonList(createApplication(user)), pageable, 1)

        BDDMockito.given(repository.findAllByUser(user, pageable)).willReturn(expectedApplicationPages)

        val actualApplicationPages = service.getAll(user, pageable)

        Assertions.assertThat(actualApplicationPages).isEqualTo(expectedApplicationPages)
    }

    @Test
    fun saveTest() {
        val user = createUser()
        val applicationRequest = createApplicationRequest()

        BDDMockito.given(repository.save(any(Application::class.java))).will { invocation -> invocation.arguments[0] }

        val savedApplication = service.save(applicationRequest, user)

        Assertions.assertThat(savedApplication.name).isEqualTo(applicationRequest.name)

    }

    private fun createUser(): User = User("104113085667282103363", 0, Collections.emptySet(), Collections.emptySet())

    private fun createApplication(user: User): Application =
        Application("Gateway 1",user, "https://openfuture.io/webhook",true,"access","key")

    private fun createApplicationRequest(): ApplicationRequest =
        ApplicationRequest("Gateway 1", true,"https://openfuture.io/webhook")
}