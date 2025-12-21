package com.finalexam.musicboxx.playlist

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlistList = ArrayList<Playlist>()
    private lateinit var tvPlaylistCount: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ---------- View ----------
        tvPlaylistCount = view.findViewById(R.id.tvPlaylistCount)
        val rvPlaylists = view.findViewById<RecyclerView>(R.id.rvPlaylists)
        val btnAddNewTop = view.findViewById<LinearLayout>(R.id.btnAddNewTop)


        // Search (giữ nguyên)
        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            requireActivity()
                .findNavController(R.id.nav_host_fragment)
                .navigate(R.id.searchFragment)
        }

        // Add playlist
        btnAddNewTop.setOnClickListener {
            showAddPlaylistDialog()
        }

        // ---------- Adapter ----------
        playlistAdapter = PlaylistAdapter(
            playlistList,
            onClick = { playlist ->
                val bundle = Bundle()
                bundle.putSerializable("playlist_data", playlist)

                // 🔥 NAVIGATION ĐÚNG – DÙNG NAVCONTROLLER GỐC
                requireActivity()
                    .findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.playlistDetailFragment, bundle)
            },
            onDeleteClick = { playlist ->
                confirmDeletePlaylist(playlist)
            }
        )

        rvPlaylists.adapter = playlistAdapter
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        // ---------- Data ----------
        fetchPlaylists()
    }

    // ================= ADD PLAYLIST =================
    private fun showAddPlaylistDialog() {
        val context = context ?: return
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_playlist)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val etName = dialog.findViewById<EditText>(R.id.etPlaylistName)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnCreate = dialog.findViewById<AppCompatButton>(R.id.btnCreate)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnCreate.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isNotEmpty()) {
                createNewPlaylist(name)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    // ================= CREATE =================
    private fun createNewPlaylist(name: String) {
        val db = FirebaseFirestore.getInstance()
        val newPlaylist = hashMapOf(
            "name" to name,
            "artist" to "User",
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("playlists")
            .add(newPlaylist)
            .addOnSuccessListener {
                Toast.makeText(context, "Created successfully!", Toast.LENGTH_SHORT).show()
                fetchPlaylists()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to create", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= DELETE CONFIRM =================
    private fun confirmDeletePlaylist(playlist: Playlist) {
        val context = context ?: return
        AlertDialog.Builder(context)
            .setTitle("Delete Playlist")
            .setMessage("Are you sure you want to delete '${playlist.name}'?")
            .setPositiveButton("Delete") { _, _ ->
                deletePlaylistFromFirebase(playlist)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ================= DELETE =================
    private fun deletePlaylistFromFirebase(playlist: Playlist) {
        if (playlist.id.isEmpty()) {
            Toast.makeText(context, "Error: Cannot find playlist ID", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("playlists")
            .document(playlist.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
                fetchPlaylists()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // ================= FETCH =================
    private fun fetchPlaylists() {
        val db = FirebaseFirestore.getInstance()
        db.collection("playlists")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                playlistList.clear()
                if (!documents.isEmpty) {
                    val tempList = ArrayList<Playlist>()
                    for (document in documents) {
                        val item = document.toObject(Playlist::class.java)
                        tempList.add(item.copy(id = document.id))
                    }
                    playlistList.addAll(tempList)
                }
                tvPlaylistCount.text = "${playlistList.size} playlists"
                playlistAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                playlistList.clear()
                tvPlaylistCount.text = "0 playlists"
                playlistAdapter.notifyDataSetChanged()
            }
    }
}
