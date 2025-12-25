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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistSongAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private var currentPlaylist: Playlist? = null

    // Kết nối ViewModel chung
    private val viewModel: HomeTabViewModel by activityViewModels()

    // --- KHAI BÁO BIẾN ---
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

        val btnShuffle: View = view.findViewById(R.id.btnShuffle)
        val btnPlay: View = view.findViewById(R.id.btnPlay)
        rvSongs = view.findViewById(R.id.rvSongs)

        currentPlaylist = arguments?.getSerializable("playlist_data") as? Playlist

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

        // MENU CỦA PLAYLIST
        btnMenu.setOnClickListener {
            showPlaylistMenu(it)
        }

        // --- NÚT PLAY ALL (PHÁT TẤT CẢ) ---
        btnPlay.setOnClickListener {
            if (songList.isNotEmpty()) {
                // Phát từ bài đầu tiên (index 0)
                viewModel.playUserList(songList, 0)
                Toast.makeText(context, "Phát playlist: ${songList.size} bài", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Playlist trống", Toast.LENGTH_SHORT).show()
            }
        }

        // --- NÚT SHUFFLE ---
        btnShuffle.setOnClickListener {
            if (songList.isNotEmpty()) {
                // Logic shuffle: Bật chế độ shuffle trước rồi phát
                viewModel.toggleShuffle()
                viewModel.playUserList(songList, 0)
                Toast.makeText(context, "Đã bật chế độ trộn bài", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ================= CẤU HÌNH DANH SÁCH =================
    private fun setupRecyclerView() {
        playlistAdapter = PlaylistSongAdapter(
            songs = songList,
            // SỰ KIỆN CLICK VÀO BÀI HÁT
            onSongClick = { song ->
                // Tìm vị trí bài hát trong list
                val index = songList.indexOf(song)
                if (index != -1) {
                    // Dùng playUserList để nạp cả list vào ExoPlayer
                    viewModel.playUserList(songList, index)
                }
            },
            // SỰ KIỆN 3 CHẤM (XÓA BÀI HÁT)
            onMoreClick = { view, song ->
                showSongOptionMenu(view, song)
            }
        )
        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = playlistAdapter
    }

    // ================= MENU XÓA BÀI HÁT (GIỮ NGUYÊN) =================
    private fun showSongOptionMenu(view: View, song: Song) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Remove from Playlist")

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
        val songDocId = song.id.ifEmpty { song.title }

        db.collection("playlists").document(playlistId)
            .collection("songs").document(songDocId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Đã xóa bài hát", Toast.LENGTH_SHORT).show()
                songList.remove(song)
                playlistAdapter.notifyDataSetChanged()

                // Cập nhật số lượng bài hát hiển thị
                view?.findViewById<TextView>(R.id.tvDetailInfo)?.text =
                    "${currentPlaylist?.artist} • ${songList.size} songs"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi khi xóa: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= TẢI BÀI HÁT (GIỮ NGUYÊN) =================
    private fun fetchSongsInPlaylist(playlistId: String) {
        db.collection("playlists").document(playlistId)
            .collection("songs")
            .orderBy("title", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                songList.clear()
                for (document in result) {
                    val song = document.toObject(Song::class.java)
                    song.id = document.id
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
    }

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
                        Toast.makeText(requireContext(), "Deleted successfully", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}