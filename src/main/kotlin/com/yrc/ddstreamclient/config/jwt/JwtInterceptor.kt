package com.yrc.ddstreamclient.config.jwt

import com.yrc.common.exception.common.EnumCommonException
import com.yrc.common.exception.common.ParametersMissingExceptionFactory
import com.yrc.common.service.jwt.JwtService
import org.apache.commons.io.IOUtils
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtInterceptor(private val jwtService: JwtService) : HandlerInterceptor{
    companion object {
        const val JWS_KEY = "jws"
    }
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val jws = request.getHeader(JWS_KEY)
            ?: throw ParametersMissingExceptionFactory
            .getHeaderParametersMissingException(listOf(JWS_KEY))
        val data: String = when (request.method) {
            "GET", "DELETE" -> {
                request.requestURI
            }
            "POST" -> {
                IOUtils.toString(request.inputStream, Charsets.UTF_8)
            }
            else -> {
                throw EnumCommonException.UNKNOWN_METHOD.build()
            }
        }
        jwtService.decode(data, jws,120 * 1000)
        return true
    }


}