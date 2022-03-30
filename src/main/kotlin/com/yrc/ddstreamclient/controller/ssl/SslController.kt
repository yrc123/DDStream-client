package com.yrc.ddstreamclient.controller.ssl

import com.yrc.ddstreamclient.service.ssl.SwappableSslService
import nl.altindag.ssl.util.KeyManagerUtils
import nl.altindag.ssl.util.KeyStoreUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream


@RestController
@RequestMapping("/api/client")
class SslController(private val sslService: SwappableSslService) {
    //    @PostMapping(value = ["/admin/ssl"], consumes = [MediaType.APPLICATION_JSON_VALUE])
//    @Throws(IOException::class)
//    fun updateKeyManager(@RequestBody request: SSLUpdateRequest) {
//        ByteArrayInputStream(request.getKeyStore()).use { keyStoreStream ->
//            ByteArrayInputStream(request.getTrustStore()).use { trustStoreStream ->
//                val keyStore: KeyStore = KeyStoreUtils.loadKeyStore(keyStoreStream, request.getKeyStorePassword())
//                val keyManager: X509ExtendedKeyManager = KeyManagerUtils.createKeyManager(keyStore, request.getKeyStorePassword())
//                val trustStore: KeyStore = KeyStoreUtils.loadKeyStore(trustStoreStream, request.getTrustStorePassword())
//                val trustManager: X509ExtendedTrustManager = TrustManagerUtils.createTrustManager(trustStore)
//                sslService.updateSslMaterials(keyManager, trustManager)
//            }
//        }
//    }
    @GetMapping("/ssl")
    fun updateKeyManager() {
        TODO("动态文件以及密码")
        val fileInputStream = FileInputStream("video/newkey.keystore")
        val keyStore = KeyStoreUtils.loadKeyStore(fileInputStream, "123456".toCharArray())
        val keyManager = KeyManagerUtils.createKeyManager(keyStore, "123456".toCharArray())
        sslService.updateSslMaterials(keyManager)
    }
}
