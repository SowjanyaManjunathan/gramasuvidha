package com.gramasuvidha.portal.firebase

import com.gramasuvidha.portal.firebase.FirebaseManager
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import com.google.firebase.messaging.FirebaseMessaging
import com.gramasuvidha.portal.firebase.FirebaseManager.Collections
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object FCMTokenRepository {

    fun saveToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val uid = auth.currentUser?.uid ?: "anonymous"
                db.collection(FirebaseManager.Collections.TOKENS)
                    .document(uid)
                    .set(mapOf("token" to token, "updatedAt" to System.currentTimeMillis()))
                    .await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshAndSave() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            saveToken(token)
        }
    }

    fun subscribeToUpdates() {
        FirebaseMessaging.getInstance().subscribeToTopic("project_updates")
    }
}
