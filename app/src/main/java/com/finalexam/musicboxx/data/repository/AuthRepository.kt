package com.finalexam.musicboxx.data.repository

import com.finalexam.musicboxx.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface AuthCallback {
    fun onSuccess(user: FirebaseUser?)
    fun onError(message: String)
}

class AuthRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun login(email: String, pass: String, callback: AuthCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword(email, pass).await()
                }
                callback.onSuccess(result.user)
            } catch (e: Exception) {
                callback.onError(e.message ?: "Lỗi đăng nhập")
            }
        }
    }

    fun register(email: String, pass: String, name: String, callback: AuthCallback) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val authResult = withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, pass).await()
                }
                val user = authResult.user
                if (user != null) {
                    val newUser = User(id = user.uid, name = name, email = email)
                    withContext(Dispatchers.IO) {
                        usersCollection.document(user.uid).set(newUser).await()
                    }
                }
                callback.onSuccess(user)
            } catch (e: Exception) {
                callback.onError(e.message ?: "Lỗi đăng ký")
            }
        }
    }

    fun logout() {
        auth.signOut()
    }
}