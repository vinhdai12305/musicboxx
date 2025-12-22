package com.finalexam.musicboxx.playlist

import Song
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistSongAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private var currentPlaylist: Playlist? = null

    // --- 1. KHAI BÁO BIẾN ---
    private lateinit var rvSongs: RecyclerView
    private lateinit var playlistAdapter: PlaylistSongAdapter
    private val songList = ArrayList<Song>()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName: TextView = view.findViewById(R.id.tvDetailName)
        val tvInfo: TextView = view.findViewById(R.id.tvDetailInfo)
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val btnBack: ImageView = view.findViewById(R.id.btnBack)
        val btnMenu: ImageView = view.findViewById(R.id.btnMenu)

        // --- ÁNH XẠ VIEW MỚI ---
        val btnShuffle: View = view.findViewById(R.id.btnShuffle)
        val btnPlay: View = view.findViewById(R.id.btnPlay)
        rvSongs = view.findViewById(R.id.rvSongs)

        currentPlaylist =
            arguments?.getSerializable("playlist_data") as? Playlist

        // --- CÀI ĐẶT RECYCLERVIEW ---
        setupRecyclerView()

        currentPlaylist?.let { playlist ->
            tvName.text = playlist.name
            tvInfo.text = "${playlist.artist} • ${playlist.songCount}"

            if (playlist.imageUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(playlist.imageUrl)
                    .into(ivCover)
            }

            // --- GỌI HÀM TẢI BÀI HÁT ---
            fetchSongsInPlaylist(playlist.id)
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // MENU CỦA PLAYLIST (Góc trên cùng)
        btnMenu.setOnClickListener {
            showPlaylistMenu(it)
        }

        // --- SỰ KIỆN NÚT PLAY & SHUFFLE ---
        btnPlay.setOnClickListener {
            if (songList.isNotEmpty()) {
                Toast.makeText(context, "Phát tất cả: ${songList.size} bài", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Playlist trống", Toast.LENGTH_SHORT).show()
            }
        }

        btnShuffle.setOnClickListener {
            Toast.makeText(context, "Đã bật chế độ trộn bài", Toast.LENGTH_SHORT).show()
        }
    }

    // ================= [ĐÃ SỬA] CẤU HÌNH DANH SÁCH =================
    private fun setupRecyclerView() {
        // Cập nhật Adapter để xử lý thêm sự kiện click 3 chấm (onMoreClick)
        playlistAdapter = PlaylistSongAdapter(
            songs = songList,
            onSongClick = { song ->
                Toast.makeText(context, "Đang phát: ${song.title}", Toast.LENGTH_SHORT).show()
            },
            // 👇 XỬ LÝ SỰ KIỆN 3 CHẤM CỦA BÀI HÁT TẠI ĐÂY
            onMoreClick = { view, song ->
                showSongOptionMenu(view, song)
            }
        )
        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = playlistAdapter
    }

    // ================= [MỚI] HIỆN MENU XÓA BÀI HÁT =================
    private fun showSongOptionMenu(view: View, song: Song) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Remove from Playlist") // Thêm lựa chọn xóa

        popup.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Remove from Playlist") {
                confirmRemoveSong(song)
                true
            } else {
                false
            }
        }
        popup.show()
    }

    private fun confirmRemoveSong(song: Song) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa bài hát")
            .setMessage("Bạn có chắc muốn xóa '${song.title}' khỏi playlist này?")
            .setPositiveButton("Xóa") { _, _ ->
                removeSongFromFirebase(song)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun removeSongFromFirebase(song: Song) {
        val playlistId = currentPlaylist?.id ?: return
        // Lưu ý: ID document phải khớp với lúc thêm (dùng id hoặc title)
        val songDocId = song.id ?: song.title

        db.collection("playlists").document(playlistId)
            .collection("songs").document(songDocId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Đã xóa bài hát", Toast.LENGTH_SHORT).show()

                // Cập nhật giao diện: Xóa khỏi list và update adapter
                songList.remove(song)
                playlistAdapter.notifyDataSetChanged()

                // Cập nhật lại số lượng bài hát hiển thị text
                view?.findViewById<TextView>(R.id.tvDetailInfo)?.text =
                    "${currentPlaylist?.artist} • ${songList.size} songs"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi khi xóa: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= LOGIC CŨ: TẢI BÀI HÁT =================
    private fun fetchSongsInPlaylist(playlistId: String) {
        db.collection("playlists").document(playlistId)
            .collection("songs")
            .orderBy("title", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                songList.clear()
                for (document in result) {
                    val song = document.toObject(Song::class.java)
                    songList.add(song)
                }
                playlistAdapter.notifyDataSetChanged()
                view?.findViewById<TextView>(R.id.tvDetailInfo)?.text =
                    "${currentPlaylist?.artist} • ${songList.size} songs"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải bài hát: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= MENU PLAYLIST (GIỮ NGUYÊN) =================
    private fun showPlaylistMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menu.add("Edit Playlist Info")
        popup.menu.add("Delete Playlist")
        popup.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Edit Playlist Info" -> {
                    handleEditPlaylist()
                    true
                }

                "Delete Playlist" -> {
                    handleDeletePlaylist()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    // ================= EDIT NAME (GIỮ NGUYÊN) =================
    private fun handleEditPlaylist() {
        val playlist = currentPlaylist ?: return
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_edit_playlist_name)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val etName = dialog.findViewById<EditText>(R.id.etPlaylistName)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnSave = dialog.findViewById<TextView>(R.id.btnSave)
        etName.setText(playlist.name)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSave.setOnClickListener {
            val newName = etName.text.toString().trim()
            if (newName.isEmpty()) {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updatePlaylistName(playlist, newName)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun updatePlaylistName(playlist: Playlist, newName: String) {
        FirebaseFirestore.getInstance()
            .collection("playlists")
            .document(playlist.id)
            .update("name", newName)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Playlist renamed", Toast.LENGTH_SHORT).show()
                view?.findViewById<TextView>(R.id.tvDetailName)?.text = newName
                currentPlaylist = playlist.copy(name = newName)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Rename failed", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= DELETE PLAYLIST (GIỮ NGUYÊN) =================
    private fun handleDeletePlaylist() {
        val playlist = currentPlaylist ?: return
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Playlist")
            .setMessage("Delete '${playlist.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                FirebaseFirestore.getInstance()
                    .collection("playlists")
                    .document(playlist.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}