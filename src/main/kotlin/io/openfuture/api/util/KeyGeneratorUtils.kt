package io.openfuture.api.util

import io.openfuture.api.domain.application.ApplicationAccessKey

import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.KeyGenerator


object KeyGeneratorUtils {

    @Throws(NoSuchAlgorithmException::class)
    fun generateApplicationAccessKey() : ApplicationAccessKey {
        val generator = KeyGenerator.getInstance("HMACSHA1")
        generator.init(120)
        val accessKeyIdEncoded = generator.generateKey().encoded
        generator.init(240)
        val secretAccessKeyEncoded = generator.generateKey().encoded

        val accessKeyId = Base64.getEncoder().encodeToString(accessKeyIdEncoded)
        val secretAccessKey = Base64.getEncoder().encodeToString(secretAccessKeyEncoded)

        return ApplicationAccessKey(accessKeyId, secretAccessKey)
    }
}