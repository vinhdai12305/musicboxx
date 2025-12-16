package com.finalexam.musicboxx.homescreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
// import android.view.LayoutInflaterimport // <<<< DÒNG LỖI ĐÃ ĐƯỢC XÓA
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.activity.ArtistDetailActivity
import com.finalexam.musicboxx.adapter.ArtistAdapter
import com.finalexam.musicboxx.model.ArtistItem

class ArtistsFragment : Fragment() {

    private lateinit var artistsRecyclerView: RecyclerView
    private lateinit var artistAdapter: ArtistAdapter // Thêm dòng này để có thể cập nhật sau
    private var artistList = mutableListOf<ArtistItem>() // Thêm danh sách có thể thay đổi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // "Thổi phồng" (inflate) layout cho fragment này
        return inflater.inflate(R.layout.fragment_artists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ánh xạ RecyclerView từ layout của Fragment.
        artistsRecyclerView = view.findViewById(R.id.artists_recycler_view)

        setupRecyclerView()
        loadArtists() // Tải dữ liệu nghệ sĩ
    }

    private fun setupRecyclerView() {
        // Khởi tạo Adapter với danh sách rỗng ban đầu
        artistAdapter = ArtistAdapter(
            artistList,
            onOptionClick = { clickedArtist ->
                // Xử lý khi nhấn nút 3 chấm: Mở BottomSheet
                val bottomSheet = ArtistOptionsBottomSheet.newInstance(clickedArtist)
                bottomSheet.show(childFragmentManager, "ArtistOptionsBottomSheetTag")
            },
            onItemClick = { clickedArtist ->
                // Xử lý khi nhấn vào item: Mở màn hình chi tiết nghệ sĩ
                val intent = Intent(requireContext(), ArtistDetailActivity::class.java).apply {
                    putExtra("EXTRA_ARTIST", clickedArtist)
                }
                startActivity(intent)
            }
        )

        // Thiết lập LayoutManager và gán Adapter cho RecyclerView
        artistsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        artistsRecyclerView.adapter = artistAdapter
    }

    // Hàm này tải dữ liệu (hiện tại là dữ liệu mẫu)
    private fun loadArtists() {
        val dummyArtists = createDummyArtists()
        artistList.clear() // Xóa dữ liệu cũ
        artistList.addAll(dummyArtists) // Thêm dữ liệu mới
        artistAdapter.notifyDataSetChanged() // Báo cho Adapter biết dữ liệu đã thay đổi để vẽ lại UI
    }

    // Hàm tạo dữ liệu nghệ sĩ mẫu
    private fun createDummyArtists(): List<ArtistItem> {
        return listOf(
            // Cấu trúc: ArtistItem(id, name, imageResource, albumCount, songCount)
            // Đảm bảo các file drawable (bray, robber,...) tồn tại trong res/drawable
            ArtistItem("1", "Dương Domic", R.drawable.bray, 1, 5),
            ArtistItem("2", "Wvrdie", R.drawable.robber, 1, 3),
            ArtistItem("3", "LyLy", R.drawable.rhym, 3, 20),
            ArtistItem("4", "RPT MCK", R.drawable.mck, 2, 15),
            ArtistItem("5", "tlinh", R.drawable.tlinh, 2, 18),
            ArtistItem("6", "WEAN", R.drawable.wean, 4, 25)
        )
    }
}
