package io.openfuture.api.client

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.openfuture.api.exception.StateApiException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

object BodyConverter {

    val log: Logger = LoggerFactory.getLogger(BodyConverter::class.java)
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }


    fun <T> serialize(body: T): String {
        return objectMapper.writeValueAsString(body)
    }

    inline fun <reified T : Any> deserialize(body: String): T {
        try {
            return objectMapper.readValue(body)
        } catch (ex: IOException) {
            val message = "Error response from OPEN API:\n$body"
            log.warn(message)
            throw StateApiException(message)
        }
    }

}