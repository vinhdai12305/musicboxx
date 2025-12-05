package com.finalexam.musicboxx.data.repository

import android.util.Log
import com.finalexam.musicboxx.data.model.Song
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Interface để Java gọi được
interface JavaCallback<T> {
    fun onSuccess(data: T)
    fun onError(message: String)
}

class SongRepository(private val firestore: FirebaseFirestore) {

    private val songsCollection = firestore.collection("songs")

    // --- KOTLIN ---
    suspend fun getAllSongs(): Resource<List<Song>> {
        return try {
            val snapshot = songsCollection.orderBy("title", Query.Direction.ASCENDING).get().await()
            val songs = snapshot.toObjects(Song::class.java)
            Resource.Success(songs)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Lỗi tải nhạc")
        }
    }

    suspend fun searchSongs(query: String): Resource<List<Song>> {
        return try {
            val startStr = query
            val endStr = query + "\uf8ff"
            val snapshot = songsCollection.orderBy("title").startAt(startStr).endAt(endStr).get().await()
            val songs = snapshot.toObjects(Song::class.java)
            Resource.Success(songs)
        } catch (e: Exception) {
            Resource.Error("Không tìm thấy")
        }
    }

    suspend fun getTrendingSongs(): Resource<List<Song>> {
        return try {
            val snapshot = songsCollection.orderBy("downloadCount", Query.Direction.DESCENDING).limit(10).get().await()
            val songs = snapshot.toObjects(Song::class.java)
            Resource.Success(songs)
        } catch (e: Exception) {
            Resource.Error("Lỗi Trending")
        }
    }

    suspend fun updateDownloadCount(songId: String) {
        try {
            songsCollection.document(songId).update("downloadCount", FieldValue.increment(1)).await()
        } catch (e: Exception) {
            Log.e("SongRepo", "Lỗi update: $e")
        }
    }

    // --- JAVA BRIDGE ---
    fun getAllSongsJava(callback: JavaCallback<List<Song>>) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) { getAllSongs() }
            if (result is Resource.Success) callback.onSuccess(result.data ?: emptyList())
            else callback.onError(result.message ?: "Lỗi")
        }
    }

    fun searchSongsJava(query: String, callback: JavaCallback<List<Song>>) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) { searchSongs(query) }
            if (result is Resource.Success) callback.onSuccess(result.data ?: emptyList())
            else callback.onError(result.message ?: "Lỗi")
        }
    }
}