package com.finalexam.musicboxx.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import Song

class SongsAdapter(
    private var songs: List<Song>,
    private val onSongClick: (Song) -> Unit,
    private val onMoreClick: (Song) -> Unit
) : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        val btnMore: ImageView? = itemView.findViewById(R.id.btnMore)

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onSongClick(songs[bindingAdapterPosition])
                }
            }
            btnMore?.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onMoreClick(songs[bindingAdapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // Đảm bảo layout này tồn tại và có ImageView id là imgSong
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_card, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        // --- SỬA LỖI MẤT ẢNH TẠI ĐÂY ---
        // Hãy chọn 1 trong 2 dòng dưới đây tùy theo file Song.kt của bạn:

        // Cách 1: Nếu trong Song.kt là val albumArtUrl: String
        val imageUrl = song.albumArtUrl



        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Ảnh hiển thị khi đang tải
            .error(R.drawable.ic_launcher_background)       // Ảnh hiển thị khi lỗi link
            .into(holder.imgSong)

        holder.btnMore?.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int = songs.size

    fun updateData(newSongs: List<Song>) {
        this.songs = newSongs
        notifyDataSetChanged()
    }
}