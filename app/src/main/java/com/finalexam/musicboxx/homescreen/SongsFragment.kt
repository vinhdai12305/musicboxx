package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.OnSongOptionsClickListener
import com.finalexam.musicboxx.adapter.SongAdapter
import com.finalexam.musicboxx.model.MusicItem

/**
 * Fragment hiển thị danh sách các bài hát.
 * Class này cũng implement OnSongOptionsClickListener để nhận sự kiện click
 * từ các item trong RecyclerView thông qua Adapter.
 */
class SongsFragment : Fragment(), OnSongOptionsClickListener {

    private lateinit var songsRecyclerView: RecyclerView
    private lateinit var songAdapter: SongAdapter
    private lateinit var songCountTextView: TextView
    private lateinit var sortButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Nạp layout cho fragment này từ file fragment_songs.xml
        return inflater.inflate(R.layout.fragment_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ánh xạ các View từ layout sau khi view đã được tạo
        songsRecyclerView = view.findViewById(R.id.songs_recycler_view)
        songCountTextView = view.findViewById(R.id.song_count_text)
        sortButton = view.findViewById(R.id.sort_button)

        // Thiết lập RecyclerView và các sự kiện click
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        // Tạo dữ liệu mẫu
        val dummySongs = createDummySongs()

        // Khởi tạo Adapter với danh sách bài hát và truyền "this" (chính Fragment này)
        // làm listener để nhận lại sự kiện click.
        songAdapter = SongAdapter(dummySongs, this)

        // Cấu hình RecyclerView
        songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        songsRecyclerView.adapter = songAdapter

        // Cập nhật số lượng bài hát lên TextView
        songCountTextView.text = getString(R.string.song_count_format, dummySongs.size)
    }

    private fun setupClickListeners() {
        // Thiết lập sự kiện click cho nút sắp xếp
        sortButton.setOnClickListener {
            // Hiển thị một thông báo tạm thời
            Toast.makeText(requireContext(), "Chức năng sắp xếp sẽ được cập nhật sau.", Toast.LENGTH_SHORT).show()
            // TODO: Xử lý logic sắp xếp danh sách ở đây.
            // Ví dụ: dummySongs.sortBy { it.title }
            // songAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Đây là hàm được override từ interface OnSongOptionsClickListener.
     * Nó sẽ được gọi bởi SongAdapter khi người dùng nhấn vào nút 3 chấm trên một bài hát.
     */
    override fun onSongOptionsClicked(song: MusicItem) {
        // Khi nhận được sự kiện, tạo một instance của SongOptionsBottomSheet và truyền dữ liệu bài hát vào.
        val bottomSheet = SongOptionsBottomSheet.newInstance(song)

        // Hiển thị BottomSheet.
        // Dùng childFragmentManager vì đang gọi từ bên trong một Fragment khác.
        bottomSheet.show(childFragmentManager, "SongOptionsBottomSheetTag")
    }

    /**
     * Hàm tạo danh sách bài hát mẫu để hiển thị.
     */
    private fun createDummySongs(): List<MusicItem> {
        val songs = mutableListOf<MusicItem>()
        for (i in 1..50) {
            // Thêm một MusicItem mới vào danh sách.
            // Các tham số được truyền vào đúng với cấu trúc của data class MusicItem.
            songs.add(
                MusicItem(
                    id = i.toString(),              // ID bài hát, kiểu String
                    title = "Bài hát số $i",          // Tên bài hát, kiểu String
                    artist = "Nghệ sĩ $i",           // Tên nghệ sĩ, kiểu String
                    imageResource = R.drawable.ic_logo // ID hình ảnh trong drawable, kiểu Int
                )
            )
        }
        return songs
    }
}

