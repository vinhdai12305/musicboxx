package com.finalexam.musicboxx

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- ĐÃ XÓA PHẦN XỬ LÝ NAV TẠI ĐÂY ---
        // Lý do: HomeFragment.kt của bạn đã có code kết nối BottomNav rồi.
        // Nếu để ở đây sẽ gây lỗi Crash vì MainActivity không tìm thấy view BottomNav.
    }

    // --- GIỮ NGUYÊN HÀM PHÁT NHẠC ---
    fun playMusic(audioUrl: String) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer?.apply {
                reset()
                setDataSource(audioUrl)
                prepare()
                start()
            }
            Toast.makeText(this, "Đang phát nhạc...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}