package com.yrc.ddstreamclient.config.ssl

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean


//@Configuration
class SslConfig {
    @Bean
    fun tomcatSslStoreCustomizer(remoteSslStoreProvider: RemoteSslStoreProvider): WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
        return WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
            it.sslStoreProvider = remoteSslStoreProvider
        }
    }
}