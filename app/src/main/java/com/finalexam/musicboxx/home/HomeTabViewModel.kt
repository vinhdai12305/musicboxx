package com.finalexam.musicboxx.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
// --- QUAN TRỌNG: Import thư viện Media3 ---
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata // Import thêm cái này để set tên bài
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
    var exoPlayer: ExoPlayer? = null // Có thể để public nếu cần truy cập từ Activity
        private set

    // Lưu playlist hiện tại để xử lý Next/Prev và update UI
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

        // 3. Tải nhạc mặc định từ Firebase
        loadSongs()
    }

    private fun updateCurrentSongInfo() {
        // Lấy index bài đang hát trong Playlist
        val idx = exoPlayer?.currentMediaItemIndex ?: return

        // Cập nhật LiveData để UI (MiniPlayer) thay đổi theo
        if (currentPlaylist.isNotEmpty() && idx in currentPlaylist.indices) {
            _currentSong.value = currentPlaylist[idx]
        }
    }

    // --- LOGIC PHÁT NHẠC CHÍNH (QUAN TRỌNG) ---

    /**
     * Hàm này dùng cho cả FavoritesFragment và HomeFragment.
     * @param userSongs: Danh sách bài hát muốn phát.
     * @param startIndex: Vị trí bài bắt đầu phát (index).
     */
    fun playUserList(userSongs: List<Song>, startIndex: Int) {
        if (userSongs.isEmpty()) return

        // 1. Cập nhật playlist hiện tại trong ViewModel
        currentPlaylist = userSongs

        // 2. Tạo MediaItem CÓ METADATA (Để hiện tên trên PlayerView của Main)
        val mediaItems = userSongs.map { song ->
            MediaItem.Builder()
                .setUri(song.audioUrl)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(song.title)           // <-- Hiện tên bài
                        .setArtist(song.artist ?: "Unknown") // <-- Hiện ca sĩ
                        .build()
                )
                .build()
        }

        // 3. Setup Player
        exoPlayer?.apply {
            stop()
            clearMediaItems()
            setMediaItems(mediaItems) // Nạp list mới

            // Khôi phục trạng thái Shuffle/Repeat cũ (nếu muốn)
            shuffleModeEnabled = _isShuffle.value ?: false
            repeatMode = _repeatMode.value ?: Player.REPEAT_MODE_OFF

            // Nhảy tới bài user chọn và phát
            seekTo(startIndex, 0L)
            prepare()
            play()
        }

        // Cập nhật ngay lập tức bài hiện tại (để UI phản hồi nhanh hơn)
        if (startIndex in userSongs.indices) {
            _currentSong.value = userSongs[startIndex]
        }
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
        // Nếu vừa nghe đc vài giây thì quay lại đầu bài, nếu nghe lâu rồi thì back bài trước
        if ((exoPlayer?.currentPosition ?: 0) > 3000) {
            exoPlayer?.seekTo(0)
        } else {
            if (exoPlayer?.hasPreviousMediaItem() == true) {
                exoPlayer?.seekToPrevious()
            } else {
                exoPlayer?.seekTo(0)
            }
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