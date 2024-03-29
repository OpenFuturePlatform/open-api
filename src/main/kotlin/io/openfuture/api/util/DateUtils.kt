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

fun differenceEpochs(startTime: Long, endTime: Long): Long{
    return kotlin.math.abs(endTime - startTime)/60
}

fun currentEpochs(): Long = System.currentTimeMillis() / 1000L

fun get7hFromDate(date: LocalDateTime): LocalDateTime {
    return date.plusHours(7)
}
