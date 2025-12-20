package com.finalexam.musicboxx.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.finalexam.musicboxx.core.AppContainer
import com.google.firebase.firestore.FirebaseFirestore // QUAN TRỌNG
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await // QUAN TRỌNG
import Song

class HomeTabViewModel(
    application: Application,
    private val appContainer: AppContainer
) : AndroidViewModel(application) {

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    private var player: ExoPlayer? = null

    init {
        player = ExoPlayer.Builder(application).build()
    }

    fun loadSongs() {
        viewModelScope.launch {
            try {
                // --- ĐÃ SỬA LỖI Ở ĐÂY ---
                val db = FirebaseFirestore.getInstance()

                val snapshot = db.collection("songs").get().await()
                val songList = snapshot.toObjects(Song::class.java)
                _songs.value = songList
            } catch (e: Exception) {
                e.printStackTrace()
                _songs.value = emptyList()
            }
        }
    }

    fun playSong(song: Song) {
        val currentPlayer = player ?: return
        val url = song.audioUrl

        if (url.isNotEmpty()) {
            currentPlayer.stop()
            val mediaItem = MediaItem.fromUri(url)
            currentPlayer.setMediaItem(mediaItem)
            currentPlayer.prepare()
            currentPlayer.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }

    class Factory(private val app: Application, private val container: AppContainer) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeTabViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeTabViewModel(app, container) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}