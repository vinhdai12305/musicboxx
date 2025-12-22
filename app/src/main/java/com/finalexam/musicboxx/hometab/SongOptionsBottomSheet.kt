package com.finalexam.musicboxx.bottomsheet

import Song
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// 1. Định nghĩa Interface để giao tiếp với Fragment bên ngoài
interface SongOptionListener {
    fun onPlayNext(song: Song)
    fun onAddToQueue(song: Song)
    fun onAddToPlaylist(song: Song)
    fun onGoToAlbum(song: Song)
    fun onGoToArtist(song: Song)
    fun onDeleteSong(song: Song)
}

class SongOptionsBottomSheet(
    private val song: Song,
    private val listener: SongOptionListener // Nhận listener từ Fragment
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet_song_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- BIND DATA HEADER ---
        view.findViewById<TextView>(R.id.tvSheetTitle).text = song.title
        view.findViewById<TextView>(R.id.tvSheetArtist).text = song.artist
        val imgCover = view.findViewById<ImageView>(R.id.imgSheetCover)

        Glide.with(this)
            .load(song.albumArtUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgCover)

        // --- XỬ LÝ SỰ KIỆN CÁC NÚT ---

        // 1. Play Next
        view.findViewById<View>(R.id.optPlayNext).setOnClickListener {
            listener.onPlayNext(song)
            dismiss()
        }

        // 2. Add to Queue
        view.findViewById<View>(R.id.optAddQueue).setOnClickListener {
            listener.onAddToQueue(song)
            dismiss()
        }

        // 3. Add to Playlist
        view.findViewById<View>(R.id.optAddPlaylist).setOnClickListener {
            listener.onAddToPlaylist(song)
            dismiss()
        }

        // 4. Go to Album
        view.findViewById<View>(R.id.optGoAlbum).setOnClickListener {
            listener.onGoToAlbum(song)
            dismiss()
        }

        // 5. Go to Artist
        view.findViewById<View>(R.id.optGoArtist).setOnClickListener {
            listener.onGoToArtist(song)
            dismiss()
        }

        // 6. Details (Xử lý trực tiếp tại đây)
        view.findViewById<View>(R.id.optDetails).setOnClickListener {
            showDetailsDialog()
            // Không dismiss để user đọc xong tự tắt
        }

        // 7. Ringtone (Cần quyền hệ thống, làm mẫu cơ bản)
        view.findViewById<View>(R.id.optRingtone).setOnClickListener {
            Toast.makeText(context, "Cần cấp quyền WRITE_SETTINGS để cài nhạc chuông", Toast.LENGTH_LONG).show()
            // Code thực tế cần Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            dismiss()
        }

        // 8. Blacklist (Ví dụ)
        view.findViewById<View>(R.id.optBlacklist).setOnClickListener {
            Toast.makeText(context, "Đã thêm vào danh sách đen", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        // 9. Share (Xử lý trực tiếp tại đây - Rất dễ)
        view.findViewById<View>(R.id.optShare).setOnClickListener {
            shareSong()
            dismiss()
        }

        // 10. Delete (Hỏi xác nhận trước khi gọi listener)
        view.findViewById<View>(R.id.optDelete).setOnClickListener {
            showDeleteConfirmDialog()
        }
    }

    // --- CÁC HÀM HỖ TRỢ LOGIC ---

    private fun shareSong() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Nghe bài này đi hay lắm!")
            putExtra(Intent.EXTRA_TEXT, "Bài hát: ${song.title} - ${song.artist}\nLink: ${song.audioUrl}")
        }
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ bài hát qua"))
    }

    private fun showDetailsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Thông tin bài hát")
            .setMessage("Tên: ${song.title}\nNghệ sĩ: ${song.artist}\nĐường dẫn: ${song.audioUrl}")
            .setPositiveButton("Đóng", null)
            .show()
    }

    private fun showDeleteConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa bài hát")
            .setMessage("Bạn có chắc chắn muốn xóa bài hát '${song.title}' khỏi thiết bị không?")
            .setPositiveButton("Xóa") { _, _ ->
                listener.onDeleteSong(song) // Gọi về Fragment để xóa thật
                dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}