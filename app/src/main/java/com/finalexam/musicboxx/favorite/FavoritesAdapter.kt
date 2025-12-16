package com.aicloudflare.musicbox.favorite // Nhớ đổi package cho đúng với dự án của bạn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Song

class FavoritesAdapter(private val songs: List<Song>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    // 1. Tạo View cho từng dòng từ file xml item_song_favorite
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    // 2. Gán dữ liệu vào View
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val song = songs[position]

        // Gán thông tin bài hát
        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist

        // Gán ảnh bìa (Dùng tạm ảnh mặc định, sau này thay bằng Glide để load URL)
        // holder.imgCover.setImageResource(song.coverResId)
        holder.imgCover .setImageResource(R.drawable.ic_launcher_background)

        // --- SỰ KIỆN: Bấm nút Play tròn màu cam ---
        holder.btnPlayItem.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Play: ${song.title}", Toast.LENGTH_SHORT).show()
            // Code xử lý phát nhạc sẽ viết ở đây
        }

        // --- SỰ KIỆN: Bấm nút 3 chấm (More) -> Hiện Popup ---
        holder.btnMore.setOnClickListener {
            // Lấy Activity chứa Adapter này để gọi FragmentManager
            val context = holder.itemView.context
            val activity = context as? androidx.fragment.app.FragmentActivity

            if (activity != null) {
                // Khởi tạo BottomSheet và truyền bài hát vào
                val bottomSheet = SongOptionsBottomSheet(song)

                // Hiển thị BottomSheet
                bottomSheet.show(activity.supportFragmentManager, "SongOptionsBottomSheet")
            } else {
                Toast.makeText(context, "Lỗi: Không thể mở popup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 3. Trả về tổng số bài hát
    override fun getItemCount(): Int = songs.size

    // 4. Khai báo các thành phần trong layout item_song_favorite.xml
    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)
        val imgCover: ImageView = itemView.findViewById(R.id.imgCover)
        val btnPlayItem: ImageView = itemView.findViewById(R.id.btnPlayItem)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
    }
}