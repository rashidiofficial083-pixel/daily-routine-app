package com.example.dailyroutine

data class RoutineItem(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val taskName: String
) {
    fun toStorageString(): String {
        return "$startHour|$startMinute|$endHour|$endMinute|$taskName"
    }

    companion object {
        fun fromStorageString(data: String): RoutineItem? {
            val parts = data.split("|")
            if (parts.size < 5) return null
            return try {
                RoutineItem(
                    parts[0].toInt(),
                    parts[1].toInt(),
                    parts[2].toInt(),
                    parts[3].toInt(),
                    parts.subList(4, parts.size).joinToString("|")
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
