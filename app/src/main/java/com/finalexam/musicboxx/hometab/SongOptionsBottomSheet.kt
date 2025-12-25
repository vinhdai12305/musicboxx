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
import com.google.firebase.firestore.FirebaseFirestore

// Interface giữ nguyên
interface SongOptionListener {
    fun onFavoriteClick(song: Song, isFavorite: Boolean)
    fun onPlayNext(song: Song)
    fun onAddToQueue(song: Song)
    fun onAddToPlaylist(song: Song)
    fun onGoToAlbum(song: Song)
    fun onGoToArtist(song: Song)
    fun onDeleteSong(song: Song)
}

class SongOptionsBottomSheet(
    private val song: Song,
    private val listener: SongOptionListener
) : BottomSheetDialogFragment() {

    private var isFavorite = false // Trạng thái hiện tại
    private lateinit var btnHeart: ImageView // Khai báo biến global để dùng ở nhiều chỗ

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet_song_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View
        val tvTitle: TextView = view.findViewById(R.id.tvSheetTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvSheetArtist)
        val imgCover: ImageView = view.findViewById(R.id.imgSheetCover)

        // Gán vào biến global btnHeart để dùng trong hàm checkFavoriteStatus
        btnHeart = view.findViewById(R.id.btnSheetHeart)

        // 2. Đổ dữ liệu
        tvTitle.text = song.title
        tvArtist.text = song.artist
        Glide.with(this)
            .load(song.albumArtUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgCover)

        // 3. --- QUAN TRỌNG: KIỂM TRA TRẠNG THÁI TIM TRÊN FIREBASE ---
        // Để khi mở lên biết là Tim Cam hay Tim Xám
        checkFavoriteStatus()

        // 4. Xử lý click vào Tim
        btnHeart.setOnClickListener {
            isFavorite = !isFavorite // Đảo ngược trạng thái

            updateHeartIcon() // Cập nhật hình ảnh dựa trên trạng thái mới

            if (isFavorite) {
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show()
            }

            // Gửi sự kiện ra ngoài Fragment
            listener.onFavoriteClick(song, isFavorite)
        }

        // --- CÁC NÚT KHÁC (GIỮ NGUYÊN) ---
        setupOtherButtons(view)
    }

    // Hàm kiểm tra xem bài này đã có trong collection "favorites" chưa
    private fun checkFavoriteStatus() {
        val db = FirebaseFirestore.getInstance()
        // Dùng logic ID giống hệt bên Fragment để đảm bảo khớp dữ liệu
        val songId = song.id ?: song.title

        db.collection("favorites").document(songId)
            .get()
            .addOnSuccessListener { document ->
                // Nếu document tồn tại -> Bài hát đã được thích -> isFavorite = true
                isFavorite = document.exists()
                // Cập nhật giao diện ngay lập tức
                updateHeartIcon()
            }
            .addOnFailureListener {
                // Nếu lỗi mạng, mặc định là chưa thích
                isFavorite = false
                updateHeartIcon()
            }
    }

    // Hàm cập nhật hình ảnh icon dựa trên biến isFavorite
    private fun updateHeartIcon() {
        if (isFavorite) {
            // Đang thích -> Hiện tim cam
            btnHeart.setImageResource(R.drawable.ic_favorite_orange)
        } else {
            // Không thích -> Hiện tim xám
            btnHeart.setImageResource(R.drawable.ic_favorite_gray)
        }
        // Xóa filter màu cũ (nếu có) để hiển thị đúng màu gốc của ảnh png/xml
        btnHeart.clearColorFilter()
    }

    // Tách code xử lý các nút khác xuống dưới cho gọn và dễ nhìn
    private fun setupOtherButtons(view: View) {
        view.findViewById<View>(R.id.optPlayNext).setOnClickListener { listener.onPlayNext(song); dismiss() }
        view.findViewById<View>(R.id.optAddQueue).setOnClickListener { listener.onAddToQueue(song); dismiss() }

        // --- ĐÃ SỬA PHẦN NÀY ---
        view.findViewById<View>(R.id.optAddPlaylist).setOnClickListener {
            // Thay vì gọi listener cũ, ta mở BottomSheet mới để chọn Playlist
            val choosePlaylistSheet = ChoosePlaylistBottomSheet(song)
            choosePlaylistSheet.show(parentFragmentManager, "ChoosePlaylistBottomSheet")
            dismiss()
        }
        // -----------------------

        view.findViewById<View>(R.id.optGoAlbum).setOnClickListener { listener.onGoToAlbum(song); dismiss() }
        view.findViewById<View>(R.id.optGoArtist).setOnClickListener { listener.onGoToArtist(song); dismiss() }

        view.findViewById<View>(R.id.optDetails).setOnClickListener {
            showDetailsDialog()
        }

        view.findViewById<View>(R.id.optRingtone).setOnClickListener {
            Toast.makeText(context, "Cần cấp quyền WRITE_SETTINGS để cài nhạc chuông", Toast.LENGTH_LONG).show()
            dismiss()
        }


        view.findViewById<View>(R.id.optShare).setOnClickListener {
            shareSong()
            dismiss()
        }

        view.findViewById<View>(R.id.optDelete).setOnClickListener {
            showDeleteConfirmDialog()
        }
    }

    // --- CÁC HÀM HỖ TRỢ (GIỮ NGUYÊN) ---

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
                listener.onDeleteSong(song)
                dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}