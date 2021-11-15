package io.openfuture.api.component.keyGenerator

import io.openfuture.api.domain.application.ApplicationAccessKey
import org.springframework.stereotype.Component
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import javax.crypto.KeyGenerator

@Component
class DigitalKeyGenerator {

    @Throws(NoSuchAlgorithmException::class)
    fun generateApplicationAccessKey() : ApplicationAccessKey {

        val generator = KeyGenerator.getInstance("HmacSHA256")
        generator.init(120)
        val accessKeyIdEncoded = generator.generateKey().encoded
        generator.init(240)
        val secretAccessKeyEncoded = generator.generateKey().encoded

        val accessKeyId = Base64.getEncoder().encodeToString(accessKeyIdEncoded)
        val secretAccessKey = Base64.getEncoder().encodeToString(secretAccessKeyEncoded)

        return ApplicationAccessKey("op_${accessKeyId}", "op_${secretAccessKey}")
    }
}