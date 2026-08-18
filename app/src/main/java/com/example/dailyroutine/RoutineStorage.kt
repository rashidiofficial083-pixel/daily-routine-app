package com.example.dailyroutine

import android.content.Context

object RoutineStorage {
    private const val PREF_NAME = "routine_prefs"
    private const val KEY_ROUTINES = "routines_list"

    fun saveRoutines(context: Context, routines: List<RoutineItem>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val stringSet = routines.map { it.toStorageString() }.toSet()
        prefs.edit().putStringSet(KEY_ROUTINES, stringSet).apply()
    }

    fun loadRoutines(context: Context): List<RoutineItem> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val stringSet = prefs.getStringSet(KEY_ROUTINES, emptySet()) ?: emptySet()
        return stringSet.mapNotNull { RoutineItem.fromStorageString(it) }
            .sortedBy { it.startHour * 60 + it.startMinute }
    }
}
