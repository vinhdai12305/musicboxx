package com.finalexam.musicboxx.data.repository

import com.finalexam.musicboxx.model.ArtistItem
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ArtistRepository(
    private val firestore: FirebaseFirestore
) {

    private val artistCollection = firestore.collection("artists")

    suspend fun getAllArtists(): Resource<List<ArtistItem>> {
        return try {
            val snapshot = artistCollection.get().await()
            val artists = snapshot.toObjects(ArtistItem::class.java)
            Resource.Success(artists)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Lỗi tải artists")
        }
    }
}
