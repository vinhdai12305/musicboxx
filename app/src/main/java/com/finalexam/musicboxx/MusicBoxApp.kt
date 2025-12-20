package com.finalexam.musicboxx

import android.app.Application
import com.finalexam.musicboxx.core.AppContainer

class MusicBoxApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        // --- QUAN TRỌNG: ĐÃ XÓA HẾT CODE FirebaseOptions THỦ CÔNG ---
        // Android sẽ tự động lấy thông tin từ file google-services.json
        // Không cần gọi FirebaseApp.initializeApp(...) bằng tay nữa

        container = AppContainer(this)
    }
}