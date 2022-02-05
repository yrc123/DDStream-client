package com.yrc.ddstreamclient.config.jwt

import com.yrc.common.service.jwt.JwtService
import org.apache.commons.io.IOUtils
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtInterceptor(private val jwtService: JwtService) : HandlerInterceptor{
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val jws = request.getHeader("jws")
        val data: String = when (request.method) {
            "GET", "DELETE" -> {
                request.requestURI
            }
            "POST" -> {
                IOUtils.toString(request.inputStream, Charsets.UTF_8)
            }
            else -> {
                TODO("抛出异常")
            }
        }
        jwtService.decode(data, jws,120 * 1000)
        return true
    }


}