package com.aowen.monolith.ui.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

fun handleTimeSinceMatch(endTime: String): String {

    val pastInstant = Instant.parse(endTime)
    val pastLocalInstant = pastInstant.atZone(ZoneId.systemDefault()).toInstant()

    val nowInstant = Instant.now()
    val nowLocalInstant = nowInstant.atZone(ZoneId.systemDefault()).toInstant()

    val duration = Duration.between(pastLocalInstant, nowLocalInstant)

    return when {
        duration.toDays() > 1 -> "${duration.toDays()} days ago"
        duration.toDays().toInt() == 1 -> "1 day ago"
        duration.toHours() >= 2 -> "${duration.toHours()}hrs ago"
        duration.toHours().toInt() == 1 -> "1h ago"
        duration.toMinutes() >= 2 -> "${duration.toMinutes()} mins ago"
        duration.toMinutes().toInt() == 1 -> "1 min ago"
        duration.seconds in 5..59 -> "${duration.seconds} sec ago"
        else -> "Just now"
    }
}