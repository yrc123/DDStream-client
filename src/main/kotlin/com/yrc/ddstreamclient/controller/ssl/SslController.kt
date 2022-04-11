package com.yrc.ddstreamclient.controller.ssl

import com.yrc.ddstreamclient.config.ssl.KeyStoreFactory
import com.yrc.ddstreamclient.service.ssl.SwappableSslService
import nl.altindag.ssl.model.KeyStoreHolder
import nl.altindag.ssl.util.KeyManagerUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/client")
class SslController(
    private val sslService: SwappableSslService,
) {
    @GetMapping("/ssl")
    fun updateKeyManager() {
        val holder = KeyStoreHolder(KeyStoreFactory.getKeyStore(), "".toCharArray())
        val keyManager = KeyManagerUtils.createKeyManager(holder)
        sslService.updateSslMaterials(keyManager)
    }
}
