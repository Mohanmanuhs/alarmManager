package com.example.alarmmanager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YourApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            "food_notification", // ID
            "Food", // Name
            NotificationManager.IMPORTANCE_HIGH // Importance
        )
        // Creates the channel
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}