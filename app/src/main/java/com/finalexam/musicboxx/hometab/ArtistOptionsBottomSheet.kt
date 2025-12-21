package com.finalexam.musicboxx.hometab

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Artist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class ArtistOptionsBottomSheet(private val artist: Artist) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.layout_bottom_sheet_artist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ---------- Views ----------
        val imgArtist: ImageView = view.findViewById(R.id.bsImgArtist)
        val tvArtistName: TextView = view.findViewById(R.id.bsTvArtistName)

        val optPlay: TextView = view.findViewById(R.id.optPlay)
        val optShare: TextView = view.findViewById(R.id.optShare)
        val optAddToPlaylist: TextView = view.findViewById(R.id.optAddToPlaylist)

        // ---------- Data ----------
        tvArtistName.text = artist.name
        Glide.with(this)
            .load(artist.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgArtist)

        // ---------- Actions ----------
        optPlay.setOnClickListener {
            Toast.makeText(context, "Play ${artist.name}", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        optShare.setOnClickListener {
            Toast.makeText(context, "Share ${artist.name}", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        optAddToPlaylist.setOnClickListener {
            showChoosePlaylistDialog()
        }
    }

    // ================= CHỌN PLAYLIST =================
    private fun showChoosePlaylistDialog() {
        val context = context ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("playlists")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(context, "No playlist found", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val playlists = documents.map {
                    it.id to (it.getString("name") ?: "Unnamed")
                }

                val names = playlists.map { it.second }.toTypedArray()

                AlertDialog.Builder(context)
                    .setTitle("Add to playlist")
                    .setItems(names) { _, index ->
                        val playlistId = playlists[index].first
                        addArtistSongsToPlaylist(playlistId)
                    }
                    .show()
            }
    }

    // ================= ADD SONGS =================
    private fun addArtistSongsToPlaylist(playlistId: String) {
        val context = context ?: return
        val db = FirebaseFirestore.getInstance()

        // Lấy toàn bộ bài hát của artist
        db.collection("songs")
            .whereEqualTo("artist", artist.name)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(context, "No songs to add", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val batch = db.batch()

                documents.forEach { doc ->
                    val songData = mapOf(
                        "title" to doc.getString("title"),
                        "artist" to doc.getString("artist"),
                        "audioUrl" to doc.getString("audioUrl")
                    )

                    val songRef = db.collection("playlists")
                        .document(playlistId)
                        .collection("songs")
                        .document(doc.id)

                    batch.set(songRef, songData)
                }

                batch.commit().addOnSuccessListener {
                    Toast.makeText(context, "Added to playlist", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
    }
}
