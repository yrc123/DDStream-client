package com.yrc.ddstreamclient.config.ssl

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.security.KeyStore
import java.util.*

object KeyStoreFactory {
    fun getKeyStore(): KeyStore? {
        val ks = KeyStore.getInstance("PKCS12")
        ks.load(null, null)

        //设置开始日期，为昨天
        val notBefore = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
        //设置截至日期，为两年后
        val notAfter = Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000)

        //生成密钥对
        val keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)
        val certificate = SelfSignedCertGenerator
            .generate(keyPair,
    "SHA256WithRSAEncryption",
                notBefore,
                notAfter,
            "DD-Stream"
            )
        ks.setKeyEntry("DD-Stream", keyPair.private, "".toCharArray(), arrayOf(certificate))

        return ks
    }
}