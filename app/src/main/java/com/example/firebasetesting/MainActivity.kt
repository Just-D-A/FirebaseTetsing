package com.example.firebasetesting

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {
	companion object {
		const val TAG = "MainActivity"
		const val BACKEND_PARAM = "button_text"

	}

	private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
	private val firebaseMessagingSettings = FirebaseMessaging.getInstance()

	lateinit var helloButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		helloButton = findViewById(R.id.button_hello)

		FirebaseInstallations.getInstance().getToken(true)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					Log.d(TAG, "Installation auth token: " + task.result?.token)
				} else {
					Log.e(TAG, "Unable to get Installation auth token")
				}
			}



		startWorkWithFirebaseRemoteConfig()
		/*		startWorkWithFirebaseMessaging()
				startWorkWithFirebaseInAppMessaging()
				*/
	}



	private fun startWorkWithFirebaseRemoteConfig() {
		firebaseRemoteConfig.setConfigSettingsAsync(
			FirebaseRemoteConfigSettings.Builder()
				.setMinimumFetchIntervalInSeconds(0)
				.setFetchTimeoutInSeconds(0)
				.build()
		)

		firebaseMessagingSettings.isAutoInitEnabled = true
		firebaseRemoteConfig.fetch(0L)
			.addOnCompleteListener(this) { task ->
				if (task.isSuccessful) {
					Toast.makeText(
						this@MainActivity, "Fetch Succeeded",
						Toast.LENGTH_SHORT

					).show()

					firebaseRemoteConfig.fetchAndActivate()

					displayWelcomeMessage()
				} else {
					Toast.makeText(
						this@MainActivity, "Fetch Failed",
						Toast.LENGTH_SHORT
					).show()
				}

			}
	}

	private fun displayWelcomeMessage() {
		val newText = firebaseRemoteConfig.getString(BACKEND_PARAM)

		if (!newText.isNullOrEmpty()) {
			helloButton.text = newText
			helloButton.isVisible = true
			Log.d("RESULT", newText)
		}

	}

	/*private fun startWorkWithFirebaseMessaging() {
		firebaseMessagingSettings.token.addOnCompleteListener(OnCompleteListener { task ->
			if (!task.isSuccessful) {
				Log.w(TAG, "Fetching FCM registration token failed", task.exception)
				return@OnCompleteListener
			}

			val token = task.result

			val msg = getString(R.string.msg_token_fmt, token)
			Log.d(TAG, msg)
			Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
		})

	}*/

}

