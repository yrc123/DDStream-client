package com.yrc.ddstreamclient.config.ssl

import com.yrc.ddstreamclient.pojo.ssl.ApplicationProperty
import nl.altindag.ssl.SSLFactory
import nl.altindag.ssl.util.JettySslUtils
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.InputStream
import javax.net.ssl.SSLSessionContext
import javax.net.ssl.X509ExtendedKeyManager
import javax.net.ssl.X509ExtendedTrustManager


@Configuration
class SslConfig {
    @Bean
    fun keyManager(sslFactory: SSLFactory): X509ExtendedKeyManager {
        return sslFactory.keyManager.orElseThrow()
    }

    @Bean
    fun trustManager(sslFactory: SSLFactory): X509ExtendedTrustManager {
        return sslFactory.trustManager.orElseThrow()
    }

    @Bean
    fun serverSessionContext(sslFactory: SSLFactory): SSLSessionContext {
        return sslFactory.sslContext.serverSessionContext
    }

    @Bean
    fun sslFactory(): SSLFactory {
        return SSLFactory.builder()
            .withSwappableIdentityMaterial()
            .withIdentityMaterial(readStoreFromRemote(), "123456".toCharArray())
            .withSwappableTrustMaterial()
            .withUnsafeTrustMaterial()
            .build()
    }

    private fun readStoreFromRemote(): InputStream {
//        TODO("从远端获取证书")
        return FileInputStream("video/key.keystore")
    }

    @Bean
    fun sslContextFactory(sslFactory: SSLFactory?): SslContextFactory.Server {
        return JettySslUtils.forServer(sslFactory)
    }

    @Bean
    fun applicationProperty(
        @Value("\${server.port}") serverPort: Int,
        @Value("\${ssl.client-auth}") isClientAuthenticationRequired: Boolean,
        @Value("\${ssl.keystore-path}") keyStorePath: String?,
        @Value("\${ssl.keystore-password}") keyStorePassword: CharArray?,
    ): ApplicationProperty? {
        return ApplicationProperty().apply {
            this.serverPort = serverPort
            this.isSslClientAuth = isClientAuthenticationRequired
            this.keystorePath = keyStorePath
            this.keystorePassword = keyStorePassword
        }
    }

    @Bean
    fun webServerFactory(sslContextFactory: SslContextFactory.Server?, applicationProperty: ApplicationProperty): ConfigurableServletWebServerFactory? {
        val factory = JettyServletWebServerFactory()
        val jettyServerCustomizer = JettyServerCustomizer { server: Server ->
            val serverConnector = ServerConnector(server, sslContextFactory)
            serverConnector.port = applicationProperty.serverPort!!
            server.connectors = arrayOf(serverConnector)
        }
        factory.serverCustomizers = listOf(jettyServerCustomizer)
        return factory
    }
}
