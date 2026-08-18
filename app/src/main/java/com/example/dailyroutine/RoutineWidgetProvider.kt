package com.example.dailyroutine

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class RoutineWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                android.content.ComponentName(context, RoutineWidgetProvider::class.java)
            )
            for (id in ids) {
                updateWidget(context, manager, id)
            }
        }

        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val routines = RoutineStorage.loadRoutines(context)
            val status = RoutineChecker.getCurrentStatus(routines)

            val views = RemoteViews(context.packageName, R.layout.widget_layout)

            if (status != null) {
                views.setTextViewText(R.id.widgetTaskName, status.taskName)
                views.setTextViewText(R.id.widgetTimeRemaining, RoutineChecker.formatTimeRemaining(status.secondsRemaining))
            } else {
                views.setTextViewText(R.id.widgetTaskName, "No active task")
                views.setTextViewText(R.id.widgetTimeRemaining, "Free time")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
