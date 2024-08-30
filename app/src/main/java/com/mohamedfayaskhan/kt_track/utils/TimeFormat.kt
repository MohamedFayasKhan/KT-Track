package com.mohamedfayaskhan.kt_track.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getFormattedTime(timeInMillis: Long): String {
    val instant = Instant
        .ofEpochMilli(timeInMillis)
    val zonedDateTime = instant
        .atZone(
            ZoneId.systemDefault()
        )
    val formatter = DateTimeFormatter
        .ofPattern(
            "MMM dd, yyyy - HH:mm:ss"
        )
    return zonedDateTime
        .format(formatter)
        .toString()
}