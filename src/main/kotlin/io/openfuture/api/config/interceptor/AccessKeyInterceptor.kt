package io.openfuture.api.config.interceptor

import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AccessKeyInterceptor(
    private val accessKeyHolder: AccessKeyHolder
    ) : HandlerInterceptor {

    override fun preHandle(requests: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        accessKeyHolder.accessKey = requests.getHeader("X-API-KEY")
        return true
    }
}