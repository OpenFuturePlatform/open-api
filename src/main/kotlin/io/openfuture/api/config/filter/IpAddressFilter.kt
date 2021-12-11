package io.openfuture.api.config.filter

import io.openfuture.api.config.propety.AuthorizationProperties
import io.openfuture.api.util.getIpRange
import org.springframework.security.web.util.matcher.IpAddressMatcher
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class IpAddressFilter(
    private val properties: AuthorizationProperties
) : Filter {

    private val IPV4_LOOPBACK = "127.0.0.1"
    private val IPV6_LOOPBACK = "0:0:0:0:0:0:0:1"
    private var ipList = arrayListOf<String>()
    var allowLocalhost = true

    override fun init(filterConfig: FilterConfig?) {
        ipList = getIpRange(properties.cidr!!)
        ipList.stream().map { ip -> print(ip) }
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        println("REMOTE ADDRESS ${request.getHeader("X-Forwarded-For")}")


        if (!isAllowed(request)) {
            println("DENIED")
            deny(response)
            return
        }
        chain.doFilter(request, response)
    }

    @Throws(IOException::class)
    fun deny(res: HttpServletResponse) {
        res.sendError(HttpServletResponse.SC_NOT_FOUND)
    }

    override fun destroy() {

    }

    fun isAllowed(request: HttpServletRequest): Boolean {

        val ip = request.remoteAddr
        if (allowLocalhost && (IPV4_LOOPBACK == ip || IPV6_LOOPBACK == ip)) {
            return true
        }
        /*var uri = request.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE) as String
        if (!StringUtils.isEmpty(uri)) {
            uri = request.requestURI
            if (request.contextPath != "/" && uri.startsWith(request.contextPath)) {
                uri = uri.substring(request.contextPath.length)
            }
        }*/

        val matcher = IpAddressMatcher("192.168.1.0/24")

        if (!matcher.matches(request.getHeader("X-Forwarded-For"))) {
            return true
        }

        return false
    }
}