package com.yrc.ddstreamclient.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.yrc.common.service.jwt.JwtKeyProvider
import com.yrc.common.service.jwt.JwtService
import com.yrc.common.service.jwt.impl.JwtServiceImpl
import com.yrc.ddstreamclient.exception.common.EnumClientException
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

@Configuration
class JwtConfig {
    @Bean
    fun getJwtService(jwtKeyProvider: JwtKeyProvider, objectMapper: ObjectMapper): JwtService{
        return JwtServiceImpl(jwtKeyProvider, objectMapper)
    }
    @Bean
    fun getJwtKeyProvider(@Value("\${dd-stream.jwt-public-key}") publicKeyString: String): JwtKeyProvider{

        return DecodeJwtKeyProvider(publicKeyString)
    }

    class DecodeJwtKeyProvider(private val encodedPublicKeyBase64: String) : JwtKeyProvider{
        private var _publicKey: PublicKey = selectPublicKey()
        override fun getPrivateKey(): PrivateKey {
            throw EnumClientException.NOT_SUPPORT_ENCODE_JWS.build()
        }

        override fun getPublicKey(): PublicKey {
            return _publicKey
        }

        override fun reset() {
            synchronized(_publicKey) {
                _publicKey = selectPublicKey()
            }
        }

        private fun selectPublicKey(): PublicKey {
            val publicKeyString = Decoders.BASE64.decode(encodedPublicKeyBase64)
            return KeyFactory
                .getInstance("EC")
                .generatePublic(X509EncodedKeySpec(publicKeyString))
        }
    }
}