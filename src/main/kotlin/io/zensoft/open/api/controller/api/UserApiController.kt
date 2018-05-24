package io.zensoft.open.api.controller.api

import io.zensoft.open.api.annotation.CurrentUser
import io.zensoft.open.api.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Kadach Alexey
 */
@RestController
@RequestMapping("/api/users")
class UserApiController {

    @GetMapping("/current")
    fun getCurrent(@CurrentUser user: User) = user

}