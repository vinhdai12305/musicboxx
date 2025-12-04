package com.finalexam.musicboxx.data.repository

import com.finalexam.musicboxx.data.model.Playlist
import com.finalexam.musicboxx.data.model.Song
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

interface PlaylistCallback<T> {
    fun onSuccess(data: T)
    fun onError(message: String)
}

class PlaylistRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val playlistCollection = firestore.collection("playlists")
    private val songsCollection = firestore.collection("songs")

    private val currentUserId: String get() = auth.currentUser?.uid ?: ""

    // --- KOTLIN ---
    suspend fun createPlaylist(name: String): Resource<Boolean> {
        return try {
            val userId = if (currentUserId.isEmpty()) "guest" else currentUserId
            val newId = UUID.randomUUID().toString()
            val playlist = Playlist(id = newId, name = name, userId = userId, createdAt = Timestamp.now())
            playlistCollection.document(newId).set(playlist).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    suspend fun getUserPlaylists(): Resource<List<Playlist>> {
        return try {
            val userId = if (currentUserId.isEmpty()) "guest" else currentUserId
            val snapshot = playlistCollection.whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING).get().await()
            Resource.Success(snapshot.toObjects(Playlist::class.java))
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    suspend fun addSongToPlaylist(playlistId: String, songId: String): Resource<Boolean> {
        return try {
            playlistCollection.document(playlistId).update("songIds", FieldValue.arrayUnion(songId)).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error("Error")
        }
    }

    suspend fun getSongsInPlaylist(playlistId: String): Resource<List<Song>> {
        return try {
            val doc = playlistCollection.document(playlistId).get().await()
            val playlist = doc.toObject(Playlist::class.java) ?: return Resource.Error("Not Found")

            if (playlist.songIds.isEmpty()) return Resource.Success(emptyList())

            val songs = mutableListOf<Song>()
            for (id in playlist.songIds) {
                val songSnap = songsCollection.document(id).get().await()
                val song = songSnap.toObject(Song::class.java)
                if (song != null) songs.add(song)
            }
            Resource.Success(songs)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    // --- JAVA ---
    fun createPlaylistJava(name: String, callback: PlaylistCallback<Boolean>) {
        CoroutineScope(Dispatchers.Main).launch {
            val res = withContext(Dispatchers.IO) { createPlaylist(name) }
            if (res is Resource.Success) callback.onSuccess(true) else callback.onError(res.message ?: "")
        }
    }

    fun getUserPlaylistsJava(callback: PlaylistCallback<List<Playlist>>) {
        CoroutineScope(Dispatchers.Main).launch {
            val res = withContext(Dispatchers.IO) { getUserPlaylists() }
            if (res is Resource.Success) callback.onSuccess(res.data ?: emptyList()) else callback.onError(res.message ?: "")
        }
    }

    fun addSongToPlaylistJava(playlistId: String, songId: String, callback: PlaylistCallback<Boolean>) {
        CoroutineScope(Dispatchers.Main).launch {
            val res = withContext(Dispatchers.IO) { addSongToPlaylist(playlistId, songId) }
            if (res is Resource.Success) callback.onSuccess(true) else callback.onError(res.message ?: "")
        }
    }

    fun getSongsInPlaylistJava(playlistId: String, callback: PlaylistCallback<List<Song>>) {
        CoroutineScope(Dispatchers.Main).launch {
            val res = withContext(Dispatchers.IO) { getSongsInPlaylist(playlistId) }
            if (res is Resource.Success) callback.onSuccess(res.data ?: emptyList()) else callback.onError(res.message ?: "")
        }
    }
}