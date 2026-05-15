package com.gramasuvidha.portal.firebase

import com.gramasuvidha.portal.firebase.FirebaseManager
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import com.google.firebase.auth.FirebaseUser
import com.gramasuvidha.portal.firebase.FirebaseManager.Collections
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import kotlinx.coroutines.tasks.await

object AuthRepository {

    val currentUser: FirebaseUser? get() = auth.currentUser
    val isLoggedIn: Boolean get() = currentUser != null

    suspend fun signUp(email: String, password: String, name: String): Result<FirebaseUser> =
        runCatching {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!
            db.collection(FirebaseManager.Collections.USERS).document(user.uid).set(
                mapOf(
                    "uid"       to user.uid,
                    "name"      to name,
                    "email"     to email,
                    "role"      to "citizen",
                    "createdAt" to System.currentTimeMillis()
                )
            ).await()
            user
        }

    suspend fun login(email: String, password: String): Result<FirebaseUser> =
        runCatching {
            auth.signInWithEmailAndPassword(email, password).await().user!!
        }

    fun logout() = auth.signOut()

    suspend fun sendPasswordReset(email: String): Result<Unit> =
        runCatching { auth.sendPasswordResetEmail(email).await() }
}
