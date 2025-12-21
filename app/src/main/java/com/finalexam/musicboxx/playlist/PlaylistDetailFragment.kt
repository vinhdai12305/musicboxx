package com.finalexam.musicboxx.playlist

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore
import android.view.ViewGroup

class PlaylistDetailFragment : Fragment(R.layout.fragment_playlist_detail) {

    private var currentPlaylist: Playlist? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName: TextView = view.findViewById(R.id.tvDetailName)
        val tvInfo: TextView = view.findViewById(R.id.tvDetailInfo)
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val btnBack: ImageView = view.findViewById(R.id.btnBack)
        val btnMenu: ImageView = view.findViewById(R.id.btnMenu)

        currentPlaylist =
            arguments?.getSerializable("playlist_data") as? Playlist

        currentPlaylist?.let { playlist ->
            tvName.text = playlist.name
            tvInfo.text = "${playlist.artist} • ${playlist.songCount} songs"

            if (playlist.imageUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(playlist.imageUrl)
                    .into(ivCover)
            }
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // ✅ MENU 3 CHẤM – CHỈ DÀNH CHO DETAIL
        btnMenu.setOnClickListener {
            showPlaylistMenu(it)
        }
    }

    // ================= MENU =================
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

    // ================= EDIT NAME =================
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

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

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
                Toast.makeText(
                    requireContext(),
                    "Playlist renamed",
                    Toast.LENGTH_SHORT
                ).show()

                // Update UI ngay lập tức
                view?.findViewById<TextView>(R.id.tvDetailName)?.text = newName
                currentPlaylist = playlist.copy(name = newName)
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Rename failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // ================= DELETE =================
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
                        Toast.makeText(
                            requireContext(),
                            "Deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
