package com.yrc.ddstreamclient.service.ssl

import nl.altindag.ssl.util.KeyManagerUtils
import nl.altindag.ssl.util.SSLSessionUtils
import org.springframework.stereotype.Service
import javax.net.ssl.SSLSessionContext
import javax.net.ssl.X509ExtendedKeyManager
import javax.net.ssl.X509ExtendedTrustManager


@Service
class SwappableSslService(
    private val sslSessionContext: SSLSessionContext,
    private val swappableKeyManager: X509ExtendedKeyManager,
    private val swappableTrustManager: X509ExtendedTrustManager
) {

    fun updateSslMaterials(keyManager: X509ExtendedKeyManager) {

        KeyManagerUtils.swapKeyManager(swappableKeyManager, keyManager)
        SSLSessionUtils.invalidateCaches(sslSessionContext)
    }
}
