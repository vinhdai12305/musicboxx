package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Song

class SongsAdapter(
    private val songs: List<Song>,
    private val onItemClick: (Song) -> Unit,
    private val onItemLongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_simple, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song)

        holder.itemView.setOnClickListener {
            onItemClick(song)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(song)
            true
        }
    }

    override fun getItemCount(): Int = songs.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.song_title)
        private val tvArtist: TextView = itemView.findViewById(R.id.artist_name_small)
        private val imgCover: ImageView = itemView.findViewById(R.id.album_cover)

        fun bind(song: Song) {
            tvTitle.text = song.title
            tvArtist.text = song.artist
            imgCover.setImageResource(song.coverArtRes)
        }
    }
}
