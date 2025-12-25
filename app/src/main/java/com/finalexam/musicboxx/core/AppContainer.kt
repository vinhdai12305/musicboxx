package com.finalexam.musicboxx.core

import android.content.Context
import com.finalexam.musicboxx.data.repository.SongRepository
import com.finalexam.musicboxx.data.repository.UserRepository
import com.finalexam.musicboxx.data.repository.DownloadRepository
import com.finalexam.musicboxx.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer(private val context: Context) {

    // 1. Khởi tạo các instance Firebase (Lazy để tiết kiệm tài nguyên)
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val authInstance: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // 2. Khởi tạo các Repositories xử lý dữ liệu
    val songRepository: SongRepository by lazy { SongRepository(firestoreInstance) }
    val userRepository: UserRepository by lazy { UserRepository(firestoreInstance, authInstance) }
    val downloadRepository: DownloadRepository by lazy { DownloadRepository(context) }
    val authRepository: AuthRepository by lazy { AuthRepository(authInstance, firestoreInstance) }

    // 3. Khởi tạo Manager quản lý phát nhạc (ExoPlayer)
    // Dùng lazy để player chỉ được tạo ra khi thực sự cần phát nhạc
    val playerManager: MusicPlayerManager by lazy { MusicPlayerManager(context) }
}