package com.finalexam.musicboxx

import android.app.Application
import android.util.Log
import com.finalexam.musicboxx.core.AppContainer
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class MusicBoxApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        // Hardcode Key để tránh lỗi google-services.json
        val myOptions = FirebaseOptions.Builder()
            .setApiKey("AIzaSyAarZyrcVjBKqIhz4rp07HMai_V7IlDkgQ")
            .setApplicationId("1:692051298480:android:b6495a80748f5a988b5375")
            .setProjectId("music-box-app-2309")
            .setStorageBucket("music-box-app-2309.firebasestorage.app")
            .build()

        if (FirebaseApp.getApps(this).isEmpty()) {
            try {
                FirebaseApp.initializeApp(this, myOptions)
            } catch (e: Exception) {
                Log.e("FIREBASE", "Init Error: ${e.message}")
            }
        }

        container = AppContainer(this)
    }
}