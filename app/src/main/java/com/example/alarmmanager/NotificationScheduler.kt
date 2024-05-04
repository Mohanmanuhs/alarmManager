package com.example.alarmmanager

interface NotificationScheduler {
    fun schedule(notification: Notification,hr:Long, min:Long)
}