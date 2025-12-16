package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistCircleAdapter
import com.finalexam.musicboxx.adapter.MusicSquareAdapter
import com.finalexam.musicboxx.model.ArtistItem
import com.finalexam.musicboxx.model.MusicItem

class SuggestedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Kết nối với layout của riêng fragment này
        return inflater.inflate(R.layout.fragment_suggested, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- SETUP RECYCLERVIEW ---
        // Gọi các hàm thiết lập cho từng RecyclerView
        setupRecentlyPlayed(view)
        setupArtists(view)
        setupMostPlayed(view)
    }

    // --- CÁC HÀM THIẾT LẬP RECYCLERVIEW ---
    private fun setupRecentlyPlayed(view: View) {
        val recycler: RecyclerView = view.findViewById(R.id.recycler_recently_played)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // SỬA Ở ĐÂY: Thêm tên tham số "musicList ="
        val adapter = MusicSquareAdapter(musicList = createDummyMusicData())
        recycler.adapter = adapter
    }

    private fun setupArtists(view: View) {
        val recycler: RecyclerView = view.findViewById(R.id.recycler_artists)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // SỬA Ở ĐÂY: Thêm tên tham số "artistList =" (hoặc tên tương ứng trong adapter của bạn)
        val adapter = ArtistCircleAdapter(artistList = createDummyArtistData())
        recycler.adapter = adapter
    }

    private fun setupMostPlayed(view: View) {
        val recycler: RecyclerView = view.findViewById(R.id.recycler_most_played)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // SỬA Ở ĐÂY: Thêm tên tham số "musicList ="
        val adapter = MusicSquareAdapter(musicList = createDummyMostPlayedData())
        recycler.adapter = adapter
    }

    // --- CÁC HÀM TẠO DỮ LIỆU GIẢ (GIỮ NGUYÊN) ---
    private fun createDummyMusicData(): List<MusicItem> {
        val data = mutableListOf<MusicItem>()
        // SỬA LỖI: Chuyển ID thành String bằng .toString() và đảm bảo các tham số có tên
        data.add(MusicItem(id = "1", title = "Không Thời Gian", artist = "Dương Domic", imageResource = R.drawable.khong_thoi_gian))
        data.add(MusicItem(id = "2", title = "Đánh Đổi", artist = "Obito", imageResource = R.drawable.danh_doi))
        data.add(MusicItem(id = "3", title = "Năm Ấy", artist = "Đức Phúc", imageResource = R.drawable.nam_ay))
        data.add(MusicItem(id = "4", title = "Còn Gì Đẹp Hơn", artist = "Nguyễn Hùng", imageResource = R.drawable.con_gi_dep_hon))
        return data
    }

    private fun createDummyArtistData(): List<ArtistItem> {
        val data = mutableListOf<ArtistItem>()
        data.add(ArtistItem(id = "101", name = "Rhymastic", imageResource = R.drawable.rhym, albumCount = 0, songCount = 0))
        data.add(ArtistItem(id = "102", name = "Bray", imageResource = R.drawable.bray, albumCount = 0, songCount = 0))
        data.add(ArtistItem(id = "103", name = "Huslang Robber", imageResource = R.drawable.robber, albumCount = 0, songCount = 0))
        data.add(ArtistItem(id = "104", name = "MCK", imageResource = R.drawable.mck, albumCount = 0, songCount = 0))
        return data
    }

    private fun createDummyMostPlayedData(): List<MusicItem> {
        val data = mutableListOf<MusicItem>()
        // SỬA LỖI: Chuyển ID thành String bằng .toString() và đảm bảo các tham số có tên
        data.add(MusicItem(id = "201", title = "Ghé Qua", artist = "Dick & PC & Tofu", imageResource = R.drawable.ghe_qua))
        data.add(MusicItem(id = "202", title = "Còn Gì Đẹp Hơn", artist = "Nguyễn Hùng", imageResource = R.drawable.con_gi_dep_hon))
        data.add(MusicItem(id = "203", title = "Y6U", artist = "Nghệ sĩ", imageResource = R.drawable.y6u))
        data.add(MusicItem(id = "204", title = "1000 Ánh Mắt", artist = "Shiki", imageResource = R.drawable.anhmat))
        return data
    }
}
