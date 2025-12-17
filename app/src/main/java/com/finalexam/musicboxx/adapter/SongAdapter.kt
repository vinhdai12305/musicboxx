package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.homescreen.SongOptionsBottomSheet
import com.finalexam.musicboxx.model.Song

class SongAdapter(
    private val songs: MutableList<Song>,
    private val onItemClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    // ===============================
    // VIEW HOLDER
    // ===============================
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.song_title)
        val artist: TextView = itemView.findViewById(R.id.artist_name_small)
        val cover: ImageView = itemView.findViewById(R.id.album_cover)
    }

    // ===============================
    // REQUIRED OVERRIDES
    // ===============================
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music_square, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        position: Int
    ) {
        val song = songs[position]

        holder.title.text = song.title
        holder.artist.text = song.artist
        holder.cover.setImageResource(song.coverArtRes)

        // 👉 CLICK → PLAY SONG
        holder.itemView.setOnClickListener {
            onItemClick(song)
        }

        // 👉 LONG CLICK → OPTIONS BOTTOM SHEET
        holder.itemView.setOnLongClickListener {
            val sheet = SongOptionsBottomSheet.newInstance(song)
            sheet.show(
                (holder.itemView.context as AppCompatActivity)
                    .supportFragmentManager,
                "SongOptions"
            )
            true
        }
    }

    override fun getItemCount(): Int = songs.size

    // ===============================
    // OPTIONAL: UPDATE DATA
    // ===============================
    fun updateData(newSongs: List<Song>) {
        songs.clear()
        songs.addAll(newSongs)
        notifyDataSetChanged()
    }
}
