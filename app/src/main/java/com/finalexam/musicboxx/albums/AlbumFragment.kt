package com.finalexam.musicboxx.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager // QUAN TRỌNG: Import cái này
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R

class AlbumFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. Nạp giao diện fragment_album
        val view = inflater.inflate(R.layout.fragment_album, container, false)

        // 2. Tạo dữ liệu giả để test (Dummy Data)
        // Trong file AlbumFragment.kt

        val listAlbum = listOf(
            // Thay toàn bộ hình bằng R.drawable.ic_launcher_background
            Album(1, "Tâm Linh", "LYHAN", "2025", "8 songs", R.drawable.ic_launcher_background),
            Album(2, "Giảm Cảm Giác", "Minh Đinh", "2025", "10 songs", R.drawable.ic_launcher_background),
            Album(3, "Nhà - EP", "52Hz", "2024", "6 songs", R.drawable.ic_launcher_background),
            Album(4, "Vương - EP", "Hnghle", "2024", "4 songs", R.drawable.ic_launcher_background),
            Album(5, "Test Album", "Unknown", "2023", "12 songs", R.drawable.ic_launcher_background)
        )

        // 3. Tìm RecyclerView từ layout
        val rvAlbums = view.findViewById<RecyclerView>(R.id.rvAlbums)

        // 4. CÀI ĐẶT DẠNG LƯỚI (GRID) - ĐÂY LÀ CHỖ QUAN TRỌNG NHẤT
        // Số 2 nghĩa là 2 cột. Nếu muốn 3 cột thì sửa thành 3.
        rvAlbums.layoutManager = GridLayoutManager(context, 2)

        // 5. Gắn Adapter vào
        val adapter = AlbumAdapter(listAlbum)
        rvAlbums.adapter = adapter

        return view
    }
}