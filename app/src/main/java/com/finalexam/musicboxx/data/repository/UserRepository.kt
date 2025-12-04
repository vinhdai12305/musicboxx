package com.finalexam.musicboxx.data.repository

import com.finalexam.musicboxx.data.model.User
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val usersCollection = firestore.collection("users")

    suspend fun toggleFavorite(userId: String, songId: String, isAdding: Boolean): Resource<Boolean> {
        return try {
            val update = if (isAdding) FieldValue.arrayUnion(songId) else FieldValue.arrayRemove(songId)
            usersCollection.document(userId).update("favorites", update).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    suspend fun getUserFavorites(userId: String): Resource<List<String>> {
        return try {
            val snap = usersCollection.document(userId).get().await()
            val user = snap.toObject(User::class.java)
            Resource.Success(user?.favorites ?: emptyList())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }
}