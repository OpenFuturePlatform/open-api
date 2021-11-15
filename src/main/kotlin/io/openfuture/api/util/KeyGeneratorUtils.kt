package io.openfuture.api.util

import io.openfuture.api.domain.application.ApplicationAccessKey
import org.apache.commons.codec.binary.Hex
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object KeyGeneratorUtils {

    @Throws(NoSuchAlgorithmException::class)
    fun generateApplicationAccessKey() : ApplicationAccessKey {
        val keyGen = KeyPairGenerator.getInstance("DSA", "SUN")
        val random: SecureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN")
        //DSA: 512, 768, 1024
        keyGen.initialize(1024, random)

        val pair = keyGen.generateKeyPair()
        val privateKey = pair.private
        val publicKey = pair.public

        println("Private key format: " + privateKey.format);
        println("Public key format: " + publicKey.format);

        val accessKeyId = Base64.getEncoder().encodeToString(publicKey.encoded)
        val secretAccessKey = Base64.getEncoder().encodeToString(privateKey.encoded)

        return ApplicationAccessKey("op_${accessKeyId}", "op_${secretAccessKey}")

    }

    fun calcHmacSha256(secretKey : String, message : String) : String {
        val hmacSha256: ByteArray? = try {
            val mac: Mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
            mac.init(secretKeySpec)
            mac.doFinal(message.toByteArray())
        } catch (e: Exception) {
            throw RuntimeException("Failed to calculate hmac-sha256", e)
        }
        return Hex.encodeHexString(hmacSha256)
    }
}