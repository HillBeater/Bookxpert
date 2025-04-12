package com.hillbeater.bookxpert.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hillbeater.bookxpert.R
import com.hillbeater.bookxpert.activities.HomeActivity

class FirebaseMessaging : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val sharedPref = getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
        val isNotificationEnabled = sharedPref.getBoolean("is_notification_enabled", true)

        if (!isNotificationEnabled) {
            //Notification is disabled from Settings
            //Don't send Push to user
            return
        }

        createNotificationChannel()

        val title = remoteMessage.data["title"] ?: "BookXpert"
        val body = remoteMessage.data["body"] ?: "New notification from BookXpert"

        sendNotification(title, body)
    }

    private fun sendNotification(title: String, message: String) {
//        val intent = Intent(this, HomeActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )

        val notificationBuilder = NotificationCompat.Builder(this, "bookxpert_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(Color.BLUE)
            //.setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "bookxpert_channel",
                "BookXpert Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for BookXpert app notifications"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}