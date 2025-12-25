package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import Song // Đảm bảo import đúng model Song

class SongsListAdapter(
    private var songs: List<Song>,
    private val onSongClick: (Song) -> Unit,
    private val onMoreClick: (Song) -> Unit // <--- 1. BỔ SUNG THAM SỐ NÀY
) : RecyclerView.Adapter<SongsListAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ánh xạ các view từ item_song_row.xml
        val imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)

        // Bổ sung 2 nút bấm mới
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)

        init {
            // 1. Click vào cả dòng -> Phát nhạc
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onSongClick(songs[position])
                }
            }

            // 2. Click vào nút Play màu cam -> Cũng phát nhạc

            // 3. Click vào nút 3 chấm (More) -> Hiện menu
            btnMore.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // <--- 2. GỌI HÀM CALLBACK KHI BẤM NÚT MORE
                    onMoreClick(songs[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_row, parent, false) // Đảm bảo dùng layout item_song_row
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        // Gán tên bài hát
        holder.tvTitle.text = song.title

        // Gán thông tin Nghệ sĩ | Thời lượng (Giả lập thời lượng vì model chưa có)
        // Kết quả sẽ là: "Son Tung M-TP | 03:50 mins"
        holder.tvArtist.text = "${song.artist} "

        // Load ảnh bìa
        Glide.with(holder.itemView.context)
            .load(song.albumArtUrl)
            .placeholder(R.drawable.ic_launcher_background) // Ảnh chờ
            .error(R.drawable.ic_launcher_background)       // Ảnh lỗi
            .into(holder.imgSong)
    }

    override fun getItemCount(): Int = songs.size

    fun updateData(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }
}