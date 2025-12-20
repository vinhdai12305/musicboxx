package com.finalexam.musicboxx.core

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class MusicPlayerManager(context: Context) {
    // Khởi tạo ExoPlayer từ thư viện Media3
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    // Hàm phát nhạc từ URL (Firebase Storage)
    fun playSong(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun resume() {
        exoPlayer.play()
    }

    // Giải phóng bộ nhớ khi không dùng nữa
    fun release() {
        exoPlayer.release()
    }

    // Trả về instance của player nếu bạn cần gắn vào UI (PlayerView)
    fun getPlayer() = exoPlayer
}