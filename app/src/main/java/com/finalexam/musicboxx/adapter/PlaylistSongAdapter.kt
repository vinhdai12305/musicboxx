package com.finalexam.musicboxx.adapter

import Song
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R

class PlaylistSongAdapter(
    private val songs: ArrayList<Song>,
    private val onSongClick: (Song) -> Unit,
    // 👇 BỔ SUNG: Callback để xử lý khi bấm vào 3 chấm
    private val onMoreClick: (View, Song) -> Unit
) : RecyclerView.Adapter<PlaylistSongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.ivSongCover)
        val title: TextView = itemView.findViewById(R.id.tvSongTitle)
        val artist: TextView = itemView.findViewById(R.id.tvSongArtist)
        val btnPlay: ImageView = itemView.findViewById(R.id.btnQuickPlay)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // Load layout item_song_playlist mới tạo
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_playlist, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.title.text = song.title
        holder.artist.text = song.artist

        Glide.with(holder.itemView.context)
            .load(song.albumArtUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.img)

        // Sự kiện Click vào toàn bộ dòng -> Phát nhạc
        holder.itemView.setOnClickListener { onSongClick(song) }

        // Sự kiện Click vào nút Play cam -> Phát nhạc
        holder.btnPlay.setOnClickListener { onSongClick(song) }

        // 👇 BỔ SUNG: Xử lý sự kiện click vào 3 chấm
        holder.btnMore.setOnClickListener { view ->
            onMoreClick(view, song)
        }
    }

    override fun getItemCount(): Int = songs.size
}