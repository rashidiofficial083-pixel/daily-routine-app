package com.example.dailyroutine

import java.util.Calendar

object RoutineChecker {

    data class CurrentStatus(
        val taskName: String,
        val minutesRemaining: Int
    )

    fun getCurrentStatus(routines: List<RoutineItem>): CurrentStatus? {
        val calendar = Calendar.getInstance()
        val nowMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

        for (item in routines) {
            val startMinutes = item.startHour * 60 + item.startMinute
            val endMinutes = item.endHour * 60 + item.endMinute

            if (nowMinutes in startMinutes until endMinutes) {
                val remaining = endMinutes - nowMinutes
                return CurrentStatus(item.taskName, remaining)
            }
        }
        return null
    }

    fun formatTimeRemaining(minutes: Int): String {
        return if (minutes >= 60) {
            val h = minutes / 60
            val m = minutes % 60
            "Time Remaining: ${h}h ${m}min"
        } else {
            "Time Remaining: ${minutes}min"
        }
    }
}
