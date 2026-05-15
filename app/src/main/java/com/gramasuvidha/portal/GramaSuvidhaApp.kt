package com.gramasuvidha.portal

import android.app.Application
import com.google.firebase.FirebaseApp

class GramaSuvidhaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}