package com.finalexam.musicboxx.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView // Khuyến nghị sử dụng ShapeableImageView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.ArtistItem

class ArtistCircleAdapter(private val artistList: List<ArtistItem>) :
    RecyclerView.Adapter<ArtistCircleAdapter.ArtistViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ArtistViewHolder {
        // Sử dụng layout item_artist_circle.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_circle, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistList[position]
        holder.artistName.text = artist.name
        // Gán hình ảnh từ Drawable ID
        holder.artistImage.setImageResource(artist.imageResource)
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    // Sử dụng ViewBinding hoặc thuộc tính lateinit cho ViewHolder để hiệu suất tốt hơn.
    // Dùng ShapeableImageView thay vì ImageView nếu layout sử dụng nó (như ảnh mẫu)
    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // SỬA LẠI CHO ĐÚNG:
        // Biến artistImage phải tham chiếu đến ID của ImageView.
        val artistImage: ShapeableImageView = itemView.findViewById(R.id.item_artist_image)

        // Biến artistName phải tham chiếu đến ID của TextView.
        // Giả sử TextView trong file XML của bạn có ID là 'item_artist_name'.
        val artistName: TextView = itemView.findViewById(R.id.item_artist_name)
    }
}
