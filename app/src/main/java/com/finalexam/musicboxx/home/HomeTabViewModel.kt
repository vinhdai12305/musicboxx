package com.finalexam.musicboxx.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
// --- QUAN TRỌNG: Import thư viện Media3 ---
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
// ------------------------------------------
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import Song // Đảm bảo import đúng model Song của bạn

class HomeTabViewModel(application: Application) : AndroidViewModel(application) {

    // --- LIVE DATA ---
    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> get() = _songs

    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> get() = _currentSong

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _isShuffle = MutableLiveData<Boolean>(false)
    val isShuffle: LiveData<Boolean> get() = _isShuffle

    private val _repeatMode = MutableLiveData<Int>(Player.REPEAT_MODE_OFF)
    val repeatMode: LiveData<Int> get() = _repeatMode

    // --- MEDIA3 PLAYER ---
    private var exoPlayer: ExoPlayer? = null

    // Lưu playlist hiện tại để xử lý Next/Prev
    private var currentPlaylist: List<Song> = emptyList()

    init {
        // 1. Khởi tạo Player (Media3)
        exoPlayer = ExoPlayer.Builder(application).build()

        // 2. Lắng nghe sự kiện
        exoPlayer?.addListener(object : Player.Listener {
            // Khi chuyển bài (tự động hoặc bấm nút)
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                updateCurrentSongInfo()
            }

            // Khi Play/Pause thay đổi
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                _isPlaying.value = isPlaying
            }

            // Khi trạng thái lặp/trộn thay đổi (đồng bộ UI nếu cần)
            override fun onRepeatModeChanged(repeatMode: Int) {
                _repeatMode.value = repeatMode
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _isShuffle.value = shuffleModeEnabled
            }
        })

        // 3. Tải nhạc
        loadSongs()
    }

    private fun updateCurrentSongInfo() {
        val idx = exoPlayer?.currentMediaItemIndex ?: return
        if (currentPlaylist.isNotEmpty() && idx < currentPlaylist.size) {
            _currentSong.value = currentPlaylist[idx]
        }
    }

    // --- LOGIC PHÁT NHẠC ---
    fun playTrack(song: Song, playlist: List<Song>) {
        if (playlist.isEmpty()) return

        currentPlaylist = playlist

        // Tìm vị trí bài hát
        val index = playlist.indexOfFirst { it.title == song.title } // Nên dùng ID nếu có
        if (index == -1) return

        // Setup Player
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()

        // Tạo MediaItem chuẩn Media3
        val mediaItems = playlist.map { MediaItem.fromUri(it.audioUrl) }
        exoPlayer?.setMediaItems(mediaItems)

        // Seek và Play
        exoPlayer?.seekTo(index, 0)
        exoPlayer?.prepare()
        exoPlayer?.play()

        // Khôi phục trạng thái Shuffle/Repeat
        exoPlayer?.shuffleModeEnabled = _isShuffle.value ?: false
        exoPlayer?.repeatMode = _repeatMode.value ?: Player.REPEAT_MODE_OFF
    }

    // --- CÁC NÚT ĐIỀU KHIỂN ---

    fun togglePlayPause() {
        if (exoPlayer?.isPlaying == true) {
            exoPlayer?.pause()
        } else {
            exoPlayer?.play()
        }
    }

    fun skipNext() {
        if (exoPlayer?.hasNextMediaItem() == true) {
            exoPlayer?.seekToNext()
        }
    }

    fun skipPrev() {
        if (exoPlayer?.hasPreviousMediaItem() == true) {
            exoPlayer?.seekToPrevious()
        } else {
            exoPlayer?.seekTo(0)
        }
    }

    fun toggleShuffle() {
        val isShuffleOn = !(_isShuffle.value ?: false)
        exoPlayer?.shuffleModeEnabled = isShuffleOn
        _isShuffle.value = isShuffleOn
    }

    fun toggleRepeat() {
        val currentMode = _repeatMode.value ?: Player.REPEAT_MODE_OFF
        val newMode = when (currentMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
        exoPlayer?.repeatMode = newMode
        _repeatMode.value = newMode
    }

    // --- SEEKBAR HELPERS ---
    fun getDuration(): Long = exoPlayer?.duration ?: 0L
    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L
    fun seekTo(pos: Long) = exoPlayer?.seekTo(pos)

    // --- FIREBASE ---
    fun loadSongs() {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance().collection("songs").get().await()
                val songList = snapshot.toObjects(Song::class.java)
                _songs.value = songList
            } catch (e: Exception) {
                Log.e("HomeTabViewModel", "Error loading songs", e)
                _songs.value = emptyList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}