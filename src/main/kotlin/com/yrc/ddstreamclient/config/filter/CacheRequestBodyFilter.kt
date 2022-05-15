package com.yrc.ddstreamclient.config.filter

import com.yrc.common.exception.common.EnumCommonException
import com.yrc.common.exception.common.ParametersMissingExceptionFactory
import com.yrc.common.service.jwt.JwtService
import com.yrc.ddstreamclient.config.jwt.JwtInterceptor
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


@Component
class CacheRequestBodyFilter(private val jwtService: JwtService) : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val requestWrapper = MultiReadHttpServletRequest(request as HttpServletRequest)
        chain.doFilter(requestWrapper, response)
    }
}