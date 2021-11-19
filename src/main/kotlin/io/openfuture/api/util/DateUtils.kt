package io.openfuture.api.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        TimeZone.getDefault().toZoneId()
    )
}

fun Date.toLocalDateTime(): LocalDateTime {
    return this.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
}

fun Long.toLocalDateTimeInSeconds(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(this),
        TimeZone.getDefault().toZoneId()
    )
}

fun LocalDateTime.toEpochMillis(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
