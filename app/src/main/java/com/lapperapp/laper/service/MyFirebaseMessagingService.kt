package com.lapperapp.laper.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lapperapp.laper.MainActivity
import com.lapperapp.laper.R
import com.lapperapp.laper.User.ProfileActivity
import com.lapperapp.laper.ui.chats.Chat.ChatActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            sendNotification(remoteMessage)
//            if (true) {
//            } else {
//                handleNow()
//            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val text = remoteMessage.data.get("text").toString()
        val title = remoteMessage.data.get("title").toString()
        val activityType = remoteMessage.data.get("activityType").toString()
        val userId = remoteMessage.data.get("userId").toString()


        var intent = Intent(this, MainActivity::class.java)
        if (activityType == Constants.mainActivity) {
            intent = Intent(this, MainActivity::class.java)
        } else if (activityType == Constants.profileActivity) {
            intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userId", userId)
        } else if (activityType == Constants.chatActivity) {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("userId", userId)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )
        val channelId = title + (0..100).random()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.rm_logo)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}