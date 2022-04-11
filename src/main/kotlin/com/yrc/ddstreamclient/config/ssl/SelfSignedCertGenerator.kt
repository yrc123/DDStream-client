package com.yrc.ddstreamclient.config.ssl

import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.X509ExtensionUtils
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.math.BigInteger
import java.security.KeyPair
import java.security.PublicKey
import java.security.cert.X509Certificate
import java.time.Instant
import java.util.*


object SelfSignedCertGenerator {

    fun generate(
        keyPair: KeyPair,
        hashAlgorithm: String?,
        notBefore: Date,
        notAfter: Date,
        cn: String,
    ): X509Certificate {
        val now = Instant.now()
        val contentSigner: ContentSigner = JcaContentSignerBuilder(hashAlgorithm).build(keyPair.private)
        val x500Name = X500Name("CN=$cn")
        val certificateBuilder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
            x500Name,
            BigInteger.valueOf(now.toEpochMilli()),
            notBefore,
            notAfter,
            x500Name,
            keyPair.public
        ).addExtension(Extension.subjectKeyIdentifier, false, createSubjectKeyId(keyPair.public))
            .addExtension(Extension.authorityKeyIdentifier, false, createAuthorityKeyId(keyPair.public))
            .addExtension(Extension.basicConstraints, true, BasicConstraints(true))
        return JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner))
    }

    private fun createSubjectKeyId(publicKey: PublicKey): SubjectKeyIdentifier {
        val publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.encoded)
        val digCalc = BcDigestCalculatorProvider()[AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)]
        return X509ExtensionUtils(digCalc).createSubjectKeyIdentifier(publicKeyInfo)
    }

    private fun createAuthorityKeyId(publicKey: PublicKey): AuthorityKeyIdentifier {
        val publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.encoded)
        val digCalc = BcDigestCalculatorProvider()[AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)]
        return X509ExtensionUtils(digCalc).createAuthorityKeyIdentifier(publicKeyInfo)
    }
}
