package com.finalexam.musicboxx.bottomsheet

import Song
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ChoosePlaylistBottomSheet(
    private val songToAdd: Song // Bài hát cần thêm
) : BottomSheetDialogFragment() {

    private lateinit var rvPlaylists: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val db = FirebaseFirestore.getInstance()
    private val playlistList = ArrayList<Playlist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_choose_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPlaylists = view.findViewById(R.id.rvChoosePlaylist)
        progressBar = view.findViewById(R.id.progressBarLoadPlaylist)

        // Cài đặt RecyclerView
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        // Gọi hàm tải dữ liệu
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        progressBar.visibility = View.VISIBLE
        // ⚠️ LƯU Ý: Đổi "playlists" thành tên collection thực tế của bạn nếu khác
        db.collection("playlists")
            .get()
            .addOnSuccessListener { result ->
                playlistList.clear()
                for (document in result) {
                    // Convert dữ liệu Firebase thành object Playlist
                    val playlist = document.toObject(Playlist::class.java)
                    // Gán ID document vào object để sau này biết lưu vào đâu
                    // (Làm thủ công vì toObject đôi khi không lấy ID)
                    val playlistWithId = playlist.copy(id = document.id)
                    playlistList.add(playlistWithId)
                }

                // Hiển thị lên list
                setupAdapter()
                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải playlist: ${it.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
    }

    private fun setupAdapter() {
        // Sử dụng Adapter nội bộ để đơn giản hóa (không có nút Delete)
        val adapter = SimplePlaylistAdapter(playlistList) { selectedPlaylist ->
            addSongToSelectedPlaylist(selectedPlaylist)
        }
        rvPlaylists.adapter = adapter
    }

    private fun addSongToSelectedPlaylist(playlist: Playlist) {
        // Logic lưu bài hát vào Sub-collection "songs" của Playlist đó
        // Cấu trúc: playlists -> {playlist_id} -> songs -> {song_id}

        val songId = songToAdd.id ?: songToAdd.title // Đảm bảo có ID

        db.collection("playlists").document(playlist.id)
            .collection("songs").document(songId)
            .set(songToAdd)
            .addOnSuccessListener {
                Toast.makeText(context, "Đã thêm vào ${playlist.name}", Toast.LENGTH_SHORT).show()
                dismiss() // Đóng hộp thoại
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // --- ADAPTER NHỎ GỌN (CHỈ DÙNG TRONG FILE NÀY) ---
    inner class SimplePlaylistAdapter(
        private val list: List<Playlist>,
        private val onItemClick: (Playlist) -> Unit
    ) : RecyclerView.Adapter<SimplePlaylistAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val img: ImageView = view.findViewById(R.id.ivPlaylistCover)
            val name: TextView = view.findViewById(R.id.tvPlaylistName)
            val more: ImageView = view.findViewById(R.id.ivMore)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Tận dụng lại layout item_playlist có sẵn của bạn
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_playlist, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val playlist = list[position]
            holder.name.text = playlist.name

            // Ẩn nút 3 chấm đi vì đây là màn hình chọn, không phải quản lý
            holder.more.visibility = View.GONE

            Glide.with(holder.itemView.context)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.img_playlist) // Ảnh mặc định của bạn
                .into(holder.img)

            holder.itemView.setOnClickListener {
                onItemClick(playlist)
            }
        }

        override fun getItemCount(): Int = list.size
    }
}