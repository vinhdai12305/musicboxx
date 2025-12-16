package com.finalexam.musicboxx.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R

// Tạo class dữ liệu đơn giản cho bài hát
data class Song(val title: String, val artist: String)

class SongAdapter(private val songList: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvSongArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_list, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist
    }

    override fun getItemCount(): Int = songList.size
}