package io.openfuture.api.component.keyGenerator

import io.openfuture.api.domain.application.ApplicationAccessKey
import org.springframework.stereotype.Component
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

@Component
class DigitalKeyGenerator {

    @Throws(NoSuchAlgorithmException::class)
    fun generateApplicationAccessKey() : ApplicationAccessKey {

        val keyGen = KeyPairGenerator.getInstance("DSA", "SUN")
        val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN")
        keyGen.initialize(512, random)

        val pair = keyGen.generateKeyPair()
        val privateKey = pair.private
        val publicKey = pair.public

        val accessKeyId = Base64.getEncoder().encodeToString(publicKey.encoded)
        val secretAccessKey = Base64.getEncoder().encodeToString(privateKey.encoded)

        return ApplicationAccessKey(accessKeyId, secretAccessKey)
    }
}