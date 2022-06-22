package com.example.donation.extetion

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun String.toLocalDate(): LocalDate {
    val values = DATE_MATCHER.find(this)?.groupValues?.map { it.toInt() } ?: throw Exception(INVALID_STRING)
    return LocalDate.of(values[3], values[2], values[1])
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toSingleString(): String {
    fun day() = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
    fun month() = if (monthValue < 10) "0$monthValue" else monthValue
    fun year() = year

    return "${day()}${month()}${year()}"
}

private const val INVALID_STRING = "Esse valor não é uma data valida"
private val DATE_MATCHER = Regex("([0-9]{2})([0-9]{2})([0-9]{4})")
