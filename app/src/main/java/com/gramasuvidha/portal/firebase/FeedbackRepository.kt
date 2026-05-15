package com.gramasuvidha.portal.firebase

import com.gramasuvidha.portal.firebase.FirebaseManager
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import com.gramasuvidha.portal.firebase.FirebaseManager.Collections
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import kotlinx.coroutines.tasks.await

object FeedbackRepository {

    suspend fun submitRating(projectId: String, rating: Int): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: "anonymous"
        db.collection(FirebaseManager.Collections.FEEDBACK).add(
            mapOf(
                "projectId" to projectId,
                "uid"       to uid,
                "rating"    to rating,
                "createdAt" to System.currentTimeMillis()
            )
        ).await()
    }

    suspend fun reportIssue(projectId: String, description: String): Result<String> = runCatching {
        val uid = auth.currentUser?.uid ?: "anonymous"
        val ref = db.collection(FirebaseManager.Collections.ISSUES).add(
            mapOf(
                "projectId"   to projectId,
                "uid"         to uid,
                "description" to description,
                "status"      to "open",
                "createdAt"   to System.currentTimeMillis()
            )
        ).await()
        ref.id
    }
}
