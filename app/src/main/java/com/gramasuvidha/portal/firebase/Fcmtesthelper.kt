package com.gramasuvidha.portal.firebase

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

object FCMTestHelper {

    const val NOTIF_PERMISSION_CODE = 1001
    private const val TAG = "FCM_Token"

    fun setup(activity: Activity) {
        requestNotificationPermission(activity)
        fetchAndLogToken(activity)
        subscribeToTopic()
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    NOTIF_PERMISSION_CODE
                )
            }
        }
    }

    fun fetchAndLogToken(activity: Activity) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d(TAG, "=== FCM TOKEN ===")
                Log.d(TAG, token)
                Log.d(TAG, "================")
                FCMTokenRepository.saveToken(token)
                Toast.makeText(activity, "FCM Ready ✓", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Token failed: ${e.message}")
            }
    }

    fun subscribeToTopic() {
        FirebaseMessaging.getInstance()
            .subscribeToTopic("project_updates")
            .addOnSuccessListener { Log.d(TAG, "Subscribed to project_updates") }
    }
}
