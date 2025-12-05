package com.finalexam.musicboxx.core

import android.content.Context
import com.finalexam.musicboxx.data.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer(private val context: Context) {

    // Khởi tạo các instance Firebase (Lazy để tiết kiệm)
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val authInstance: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    // Khởi tạo các Repository (Công cụ)
    val songRepository: SongRepository by lazy { SongRepository(firestoreInstance) }
    val userRepository: UserRepository by lazy { UserRepository(firestoreInstance, authInstance) }
    val downloadRepository: DownloadRepository by lazy { DownloadRepository(context) }
    val playlistRepository: PlaylistRepository by lazy { PlaylistRepository(firestoreInstance, authInstance) }
    val authRepository: AuthRepository by lazy { AuthRepository(authInstance, firestoreInstance) }
}