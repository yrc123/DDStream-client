package com.yrc.ddstreamclient.config

import com.yrc.common.service.jwt.JwtService
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    @Resource
    lateinit var jwtService: JwtService
    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(JwtInterceptor(jwtService))
    }

}