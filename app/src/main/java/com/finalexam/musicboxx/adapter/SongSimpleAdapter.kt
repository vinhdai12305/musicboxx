package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.databinding.ItemSongSimpleBinding // Đảm bảo import này đúng
import com.finalexam.musicboxx.model.Song

class SongSimpleAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongSimpleAdapter.SongViewHolder>() {

    // ViewHolder sử dụng ViewBinding
    inner class SongViewHolder(private val binding: ItemSongSimpleBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Thiết lập listener cho toàn bộ item view
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onSongClick(songs[adapterPosition])
                }
            }
        }

        fun bind(song: Song) {
            binding.songTitleSimple.text = song.title
            binding.songArtistSimple.text = song.artist

            // Dùng try-catch để an toàn khi set ảnh
            try {
                binding.songAlbumArtSimple.setImageResource(song.coverArtRes)
            } catch (e: Exception) {
                // Nếu có lỗi, set ảnh mặc định
                binding.songAlbumArtSimple.setImageResource(com.finalexam.musicboxx.R.drawable.ic_launcher_background)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // "Thổi phồng" layout bằng ViewBinding
        val binding = ItemSongSimpleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}
