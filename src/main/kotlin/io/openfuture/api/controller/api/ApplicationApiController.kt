package io.openfuture.api.controller.api


import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.component.keyGenerator.DigitalKeyGenerator
import io.openfuture.api.domain.PageRequest
import io.openfuture.api.domain.PageResponse
import io.openfuture.api.domain.application.ApplicationDto
import io.openfuture.api.domain.application.ApplicationRequest
import io.openfuture.api.entity.application.Application
import io.openfuture.api.entity.auth.User
import io.openfuture.api.service.ApplicationService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/application")
class ApplicationApiController(
    private val service: ApplicationService,
    private val digitalKeyGenerator: DigitalKeyGenerator
) {

    @GetMapping
    fun getAll(@CurrentUser user: User, pageRequest: PageRequest): PageResponse<ApplicationDto> {
        val gateways = service.getAll(user, pageRequest).map { ApplicationDto(it) }
        return PageResponse(gateways)
    }

    @PostMapping
    fun save(@Valid @RequestBody request: ApplicationRequest, @CurrentUser user: User): Application =
        service.save(request, user, digitalKeyGenerator.generateApplicationAccessKey())

    @PostMapping("/{id}")
    fun update(@PathVariable id: Long): Application =
        service.update(id, digitalKeyGenerator.generateApplicationAccessKey())

    @DeleteMapping
    fun delete(@RequestParam id: Long): Boolean {
        service.delete(id)
        return true
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ApplicationDto {
        val application = service.getById(id)
        return ApplicationDto(application)
    }

}