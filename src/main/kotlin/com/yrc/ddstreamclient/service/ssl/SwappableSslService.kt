package com.yrc.ddstreamclient.service.ssl

import nl.altindag.ssl.util.KeyManagerUtils
import nl.altindag.ssl.util.SSLSessionUtils
import nl.altindag.ssl.util.TrustManagerUtils
import org.springframework.stereotype.Service
import javax.net.ssl.SSLSessionContext
import javax.net.ssl.X509ExtendedKeyManager
import javax.net.ssl.X509ExtendedTrustManager


@Service
class SwappableSslService(
    private val sslSessionContext: SSLSessionContext,
    private val swappableKeyManager: X509ExtendedKeyManager,
    swappableTrustManager: X509ExtendedTrustManager
) {
    private val swappableTrustManager: X509ExtendedTrustManager

    init {
        this.swappableTrustManager = swappableTrustManager
    }

    fun updateSslMaterials(keyManager: X509ExtendedKeyManager?, trustManager: X509ExtendedTrustManager?) {
        KeyManagerUtils.swapKeyManager(swappableKeyManager, keyManager)
        TrustManagerUtils.swapTrustManager(swappableTrustManager, trustManager)
        SSLSessionUtils.invalidateCaches(sslSessionContext)
    }
}
