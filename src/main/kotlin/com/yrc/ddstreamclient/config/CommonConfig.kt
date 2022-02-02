package com.yrc.ddstreamclient.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class CommonConfig {
    @Value("\${spring.application.name}")
    lateinit var issuer: String

}