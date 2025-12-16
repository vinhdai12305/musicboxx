package com.finalexam.musicboxx.homescreen

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.MusicItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongOptionsBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Nạp layout cho BottomSheet
        return inflater.inflate(R.layout.bottom_sheet_song_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // KHẮC PHỤC LỖI API LEVEL: Kiểm tra phiên bản Android trước khi gọi hàm
        val song: MusicItem?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Nếu là Android 13 (API 33) trở lên, dùng hàm getParcelable mới
            song = arguments?.getParcelable("selected_song", MusicItem::class.java)
        } else {
            // Nếu là phiên bản Android cũ hơn, dùng hàm getParcelable cũ (deprecated)
            @Suppress("DEPRECATION") // Thêm chú thích này để ẩn cảnh báo không cần thiết
            song = arguments?.getParcelable("selected_song")
        }

        // Kiểm tra an toàn, nếu không có dữ liệu bài hát thì đóng BottomSheet
        if (song == null) {
            Toast.makeText(context, "Error: Song data not found", Toast.LENGTH_SHORT).show()
            dismiss()
            return
        }

        // --- Ánh xạ các view và gán dữ liệu từ đối tượng 'song' ---
        view.findViewById<TextView>(R.id.song_title_bottom_sheet).text = song.title
        // Tùy chỉnh hiển thị cho nghệ sĩ và thời lượng (bạn có thể thay đổi sau)
        val artistAndDuration = "feat. ${song.artist} | 03:50 mins"
        view.findViewById<TextView>(R.id.song_artist_bottom_sheet).text = artistAndDuration
        view.findViewById<ImageView>(R.id.song_album_art).setImageResource(song.imageResource)

        // --- Gán sự kiện click cho tất cả các tùy chọn ---
        setupOptionClickListeners(view, song)
    }

    private fun setupOptionClickListeners(view: View, song: MusicItem) {
        // Danh sách các ID của TextView và thông điệp tương ứng
        val options = mapOf(
            R.id.option_play_next to "Play Next",
            R.id.option_add_to_queue to "Add to Playing Queue",
            R.id.option_add_to_playlist to "Add to Playlist",
            R.id.option_go_to_album to "Go to Album",
            R.id.option_go_to_artist to "Go to Artist",
            R.id.option_details to "Details",
            R.id.option_set_as_ringtone to "Set as Ringtone",
            R.id.option_add_to_blacklist to "Add to Blacklist",
            R.id.option_share to "Share",
            R.id.option_delete to "Delete from Device"
        )

        // Dùng vòng lặp để gán sự kiện cho tất cả các nút
        options.forEach { (viewId, actionText) ->
            view.findViewById<TextView>(viewId)?.setOnClickListener {
                // Hiển thị một thông báo Toast mẫu
                Toast.makeText(context, "$actionText: ${song.title}", Toast.LENGTH_SHORT).show()
                // Sau khi thực hiện hành động, đóng BottomSheet
                dismiss()
            }
        }

        // Gán sự kiện riêng cho nút yêu thích
        view.findViewById<ImageView>(R.id.favorite_button)?.setOnClickListener {
            Toast.makeText(context, "Toggled Favorite for ${song.title}", Toast.LENGTH_SHORT).show()
            // TODO: Cập nhật trạng thái yêu thích của bài hát và thay đổi icon
        }
    }

    companion object {
        // Dùng const để tối ưu và tránh lỗi chính tả
        private const val SELECTED_SONG_KEY = "selected_song"

        // Hàm newInstance để tạo Fragment và truyền dữ liệu một cách an toàn
        fun newInstance(song: MusicItem): SongOptionsBottomSheet {
            val fragment = SongOptionsBottomSheet()
            val args = Bundle().apply {
                putParcelable(SELECTED_SONG_KEY, song)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
