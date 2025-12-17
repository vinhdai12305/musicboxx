package com.aicloudflare.musicbox.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Song

// Adapter nhận vào List<Song> (không null)
class AddSongsAdapter(private val songList: List<Song>) :
    RecyclerView.Adapter<AddSongsAdapter.SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_add, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songList[position]

        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        // Set ảnh bìa (Đảm bảo logic này khớp với Model Song của bạn)
        // holder.imgCover.setImageResource(song.coverResId)
        holder.imgCover.setImageResource(R.drawable.ic_playlist_gray) // Set tạm ảnh mặc định nếu chưa có

        // --- LOGIC ĐỔI ICON ---
        // Lưu ý: Class Song của bạn cần có biến `var isAdded: Boolean = false`
        /*
        if (song.isAdded) {
            holder.btnAddCheck.setImageResource(R.drawable.ic_addcheck) // Đã thêm
        } else {
            holder.btnAddCheck.setImageResource(R.drawable.ic_addbox)   // Chưa thêm
        }

        holder.btnAddCheck.setOnClickListener {
            // Đảo trạng thái
            song.isAdded = !song.isAdded
            // Cập nhật lại giao diện dòng này
            notifyItemChanged(position)
        }
        */
    }

    override fun getItemCount(): Int = songList.size

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvSongArtist)
        val imgCover: ImageView = itemView.findViewById(R.id.imgSongCover)
        val btnAddCheck: ImageView = itemView.findViewById(R.id.btnAddCheck)
    }
}