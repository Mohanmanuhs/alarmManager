package com.example.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random


class AndroidNotificationScheduler(private val context: Context):NotificationScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(notification: Notification,hr:Long,min:Long) {
        val intent = Intent(context, NotificationReceiver::class.java)
            .apply {
                putExtra("TITLE", notification.title)
                putExtra("DESCRIPTION", notification.description)
            }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            LocalDateTime.now()
                .plusSeconds((hr*60*60)+(min*60))
                .atZone(ZoneId.systemDefault())
                .toEpochSecond()*1000,
            PendingIntent.getBroadcast(
                context,
                Random.nextInt(),
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}