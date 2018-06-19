package io.openfuture.api.controller.api

import io.openfuture.api.annotation.CurrentUser
import io.openfuture.api.domain.auth.UserDto
import io.openfuture.api.entity.auth.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserApiController {

    @GetMapping("/current")
    fun getCurrent(@CurrentUser user: User): UserDto = UserDto(user)

}