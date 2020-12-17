package com.example.firebasetesting

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

	companion object {
		private const val TAG = "MyFirebaseMessagingSer"
	}

	override fun onNewToken(token: String) {
		Log.d(TAG, "Refreshed token: $token")

		sendRegistrationToServer(token)
	}

	private fun sendRegistrationToServer(token: String) {
		// TODO: Implement this method to send token to your app server.
	}

	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		Log.d(MainActivity.TAG, "From: " + remoteMessage.from)

		if (remoteMessage.data.isNotEmpty()) {
			Log.d(MainActivity.TAG, "Message data payload: " + remoteMessage.data)
		}

		if (remoteMessage.notification != null) {
			Log.d(MainActivity.TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
		}
	}
}