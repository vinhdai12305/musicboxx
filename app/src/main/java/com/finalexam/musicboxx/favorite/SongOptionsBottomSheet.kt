package com.aicloudflare.musicbox.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Song
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongOptionsBottomSheet(private val song: Song) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Nạp giao diện layout_bottom_sheet.xml mà bạn vừa sửa xong
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ các View trong BottomSheet
        val tvTitle = view.findViewById<TextView>(R.id.tvSheetTitle)
        val tvInfo = view.findViewById<TextView>(R.id.tvSheetInfo)

        // Dòng "Add to Playlist" mà bạn vừa thêm ID
        val btnAddToPlaylist = view.findViewById<TextView>(R.id.sheetOptionAddToPlaylist)

        // Dòng "Delete"
        val btnDelete = view.findViewById<TextView>(R.id.sheetOptionDelete)

        // 2. Hiển thị dữ liệu bài hát lên Header
        tvTitle.text = song.title
        tvInfo.text = "${song.artist}"

        // 3. Bắt sự kiện click: Add to Playlist
        btnAddToPlaylist.setOnClickListener {
            // Tạm thời hiện thông báo, sau này sẽ code logic hiện danh sách Playlist ở đây
            Toast.makeText(context, "Đã chọn: Thêm vào Playlist", Toast.LENGTH_SHORT).show()
            dismiss() // Đóng BottomSheet
        }

        // 4. Bắt sự kiện click: Delete
        btnDelete.setOnClickListener {
            Toast.makeText(context, "Đã chọn: Xóa bài hát", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        // Bạn có thể bắt sự kiện cho các dòng khác tương tự...
    }
}