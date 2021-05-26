package com.example.babble.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    fun onMessageRecieved(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            //TODO: Show notifications
            Log.d("FCM", remoteMessage.data.toString())
        }
    }
}