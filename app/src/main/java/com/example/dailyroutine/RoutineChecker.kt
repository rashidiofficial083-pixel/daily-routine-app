package com.example.dailyroutine

import java.util.Calendar

object RoutineChecker {

    data class CurrentStatus(
        val taskName: String,
        val secondsRemaining: Int
    )

    fun getCurrentStatus(routines: List<RoutineItem>): CurrentStatus? {
        val calendar = Calendar.getInstance()
        val nowSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 3600 +
                calendar.get(Calendar.MINUTE) * 60 +
                calendar.get(Calendar.SECOND)

        for (item in routines) {
            val startSeconds = item.startHour * 3600 + item.startMinute * 60
            val endSeconds = item.endHour * 3600 + item.endMinute * 60

            if (nowSeconds in startSeconds until endSeconds) {
                val remaining = endSeconds - nowSeconds
                return CurrentStatus(item.taskName, remaining)
            }
        }
        return null
    }

    fun formatTimeRemaining(totalSeconds: Int): String {
        val h = totalSeconds / 3600
        val m = (totalSeconds % 3600) / 60
        val s = totalSeconds % 60
        return if (h > 0) {
            "Time Remaining: ${h}h ${m}m ${s}s"
        } else {
            "Time Remaining: ${m}m ${s}s"
        }
    }
}
