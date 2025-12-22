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
import com.finalexam.musicboxx.MainActivity // Import MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistSongAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private var currentPlaylist: Playlist? = null

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
            fetchSongsInPlaylist(playlist.id)
        }

        btnBack.setOnClickListener { findNavController().popBackStack() }
        btnMenu.setOnClickListener { showPlaylistMenu(it) }

        // --- SỰ KIỆN NÚT PLAY (PHÁT TẤT CẢ) ---
        btnPlay.setOnClickListener {
            if (songList.isNotEmpty()) {
                // Vì MainActivity cũ chỉ phát được 1 bài qua URL string
                // Nên ta tạm thời phát bài đầu tiên trong danh sách
                val firstSong = songList[0]
                if (firstSong.audioUrl.isNotEmpty()) {
                    (activity as? MainActivity)?.playMusic(firstSong.audioUrl)
                    Toast.makeText(context, "Đang phát: ${firstSong.title}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Playlist trống", Toast.LENGTH_SHORT).show()
            }
        }

        btnShuffle.setOnClickListener {
            // Logic trộn bài (chỉ mang tính minh họa vì MainActivity chưa hỗ trợ list)
            if (songList.isNotEmpty()) {
                val randomSong = songList.random()
                if (randomSong.audioUrl.isNotEmpty()) {
                    (activity as? MainActivity)?.playMusic(randomSong.audioUrl)
                    Toast.makeText(context, "Phát ngẫu nhiên: ${randomSong.title}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ================= CẤU HÌNH DANH SÁCH =================
    private fun setupRecyclerView() {
        playlistAdapter = PlaylistSongAdapter(
            songs = songList,
            // --- XỬ LÝ CLICK BÀI HÁT ---
            onSongClick = { song ->
                // Kiểm tra URL có tồn tại không
                if (song.audioUrl.isNotEmpty()) {
                    // Gọi hàm playMusic(String) có sẵn trong MainActivity của bạn
                    (activity as? MainActivity)?.playMusic(song.audioUrl)
                } else {
                    Toast.makeText(context, "Link nhạc bị lỗi", Toast.LENGTH_SHORT).show()
                }
            },
            // Xử lý 3 chấm (như cũ)
            onMoreClick = { view, song ->
                showSongOptionMenu(view, song)
            }
        )
        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = playlistAdapter
    }

    // ================= CÁC HÀM KHÁC (GIỮ NGUYÊN) =================
    private fun showSongOptionMenu(view: View, song: Song) {
        val popup = PopupMenu(requireContext(), view)
        popup.menu.add("Remove from Playlist")
        popup.setOnMenuItemClickListener { menuItem ->
            if (menuItem.title == "Remove from Playlist") {
                confirmRemoveSong(song)
                true
            } else { false }
        }
        popup.show()
    }

    private fun confirmRemoveSong(song: Song) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa bài hát")
            .setMessage("Bạn có chắc muốn xóa '${song.title}' khỏi playlist này?")
            .setPositiveButton("Xóa") { _, _ -> removeSongFromFirebase(song) }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun removeSongFromFirebase(song: Song) {
        val playlistId = currentPlaylist?.id ?: return
        val songDocId = song.id ?: song.title

        db.collection("playlists").document(playlistId)
            .collection("songs").document(songDocId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Đã xóa bài hát", Toast.LENGTH_SHORT).show()
                songList.remove(song)
                playlistAdapter.notifyDataSetChanged()
                view?.findViewById<TextView>(R.id.tvDetailInfo)?.text =
                    "${currentPlaylist?.artist} • ${songList.size} songs"
            }
    }

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

    private fun showPlaylistMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menu.add("Edit Playlist Info")
        popup.menu.add("Delete Playlist")
        popup.setOnMenuItemClickListener { item ->
            when (item.title.toString()) {
                "Edit Playlist Info" -> { handleEditPlaylist(); true }
                "Delete Playlist" -> { handleDeletePlaylist(); true }
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
        dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        val etName = dialog.findViewById<EditText>(R.id.etPlaylistName)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnSave = dialog.findViewById<TextView>(R.id.btnSave)
        etName.setText(playlist.name)
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSave.setOnClickListener {
            val newName = etName.text.toString().trim()
            if (newName.isNotEmpty()) {
                updatePlaylistName(playlist, newName)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun updatePlaylistName(playlist: Playlist, newName: String) {
        FirebaseFirestore.getInstance().collection("playlists").document(playlist.id)
            .update("name", newName)
            .addOnSuccessListener {
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
                FirebaseFirestore.getInstance().collection("playlists").document(playlist.id)
                    .delete()
                    .addOnSuccessListener { findNavController().popBackStack() }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}