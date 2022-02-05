package com.yrc.ddstreamclient.config.ssl

import nl.altindag.ssl.SSLFactory
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
            .withIdentityMaterial(readStoreFromRemote(), "123456".toCharArray())
            .withUnsafeTrustMaterial()
            .build()
    }

    private fun readStoreFromRemote(): InputStream {
        return FileInputStream("hls/key.keystore")
    }
//
//    @Bean
//    fun sslContextFactory(sslFactory: SSLFactory?): SslContextFactory.Server {
//        return JettySslUtils.forServer(sslFactory)
//    }
}
