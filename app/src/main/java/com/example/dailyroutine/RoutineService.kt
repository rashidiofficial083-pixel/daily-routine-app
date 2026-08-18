package com.example.dailyroutine

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class RoutineService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateRunnable: Runnable

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)

        updateRunnable = object : Runnable {
            override fun run() {
                updateNotification()
                handler.postDelayed(this, 60000)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val routines = RoutineStorage.loadRoutines(this)
        val status = RoutineChecker.getCurrentStatus(routines)

        if (status != null) {
            val notification = buildNotification(status.taskName, RoutineChecker.formatTimeRemaining(status.minutesRemaining))
            startForeground(1001, notification)
        } else {
            val notification = buildNotification("No active task", "Free time")
            startForeground(1001, notification)
        }

        handler.removeCallbacks(updateRunnable)
        handler.postDelayed(updateRunnable, 60000)

        return START_STICKY
    }

    private fun updateNotification() {
        val routines = RoutineStorage.loadRoutines(this)
        val status = RoutineChecker.getCurrentStatus(routines)

        if (status != null) {
            NotificationHelper.showNotification(this, status.taskName, RoutineChecker.formatTimeRemaining(status.minutesRemaining))
        } else {
            NotificationHelper.showNotification(this, "No active task", "Free time")
        }
    }

    private fun buildNotification(title: String, text: String) =
        androidx.core.app.NotificationCompat.Builder(this, "routine_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setOngoing(true)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_LOW)
            .build()

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
