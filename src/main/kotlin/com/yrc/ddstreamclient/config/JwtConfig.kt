package com.yrc.ddstreamclient.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.yrc.common.service.jwt.JwtKeyProvider
import com.yrc.common.service.jwt.JwtService
import com.yrc.common.service.jwt.impl.JwtServiceImpl
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
        private val publicKey: PublicKey
        init {
            val publickey = Decoders.BASE64.decode(encodedPublicKeyBase64)
            publicKey = KeyFactory
                .getInstance("EC")
                .generatePublic(X509EncodedKeySpec(publickey))
            //私钥为PKCS8EncodedKeySpec
        }
        override fun getPrivateKey(): PrivateKey {
            TODO("Not yet implemented")
        }

        override fun getPublicKey(): PublicKey {
            return publicKey
        }
    }
}