// Top-level build file
plugins {
        id ("com.android.application") version "8.6.0" apply false
        id ("com.android.library") version "8.6.0" apply false
        //id("org.jetbrains.kotlin.android") version "2.0.21" apply false
        id ("com.google.gms.google-services") version "4.4.1" apply false
    //id("com.android.application") version "8.6.0" apply false
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false   // 🔥 REQUIRED

    }

buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.1")
    }
}