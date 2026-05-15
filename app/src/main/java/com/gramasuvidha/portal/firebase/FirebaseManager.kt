package com.gramasuvidha.portal.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseManager {

    val auth: FirebaseAuth by lazy { Firebase.auth }

    val db: FirebaseFirestore by lazy { Firebase.firestore }

    val messaging: FirebaseMessaging by lazy { FirebaseMessaging.getInstance() }

    object Collections {
        const val PROJECTS = "projects"
        const val FEEDBACK = "feedback"
        const val ISSUES = "issues"
        const val USERS = "users"
        const val TOKENS = "fcm_tokens"
    }
}
