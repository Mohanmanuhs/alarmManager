package com.example.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var appNotificationManager: AppNotificationManager

    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE") ?: return
        val description = intent.getStringExtra("DESCRIPTION") ?: return

        appNotificationManager
            .showBasicNotification(
                title = title,
                description = description
            )
    }
}