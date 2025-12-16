package com.finalexam.musicboxx.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem // <<-- THÊM IMPORT NÀY
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongSimpleAdapter
import com.finalexam.musicboxx.databinding.ActivityArtistDetailBinding
import com.finalexam.musicboxx.model.ArtistItem
import com.finalexam.musicboxx.model.Song
import java.util.concurrent.TimeUnit

class ArtistDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtistDetailBinding
    private lateinit var songAdapter: SongSimpleAdapter
    private var songList = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy dữ liệu an toàn
        val artist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_ARTIST", ArtistItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<ArtistItem>("EXTRA_ARTIST")
        }

        if (artist == null) {
            finish() // Đóng activity nếu không có dữ liệu
            return
        }

        setupToolbar()
        setupRecyclerView()
        updateArtistInfo(artist)
        loadSongsForArtist(artist.name)
    }

    // =================================================================
    // HÀM XỬ LÝ NÚT BACK TRÊN TOOLBAR
    // =================================================================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Kiểm tra xem nút được nhấn có phải là nút "home" (nút back) không
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed() // Gọi hành động quay lại mặc định
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    // =================================================================

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        // Hiển thị nút back trên Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // (Không cần setOnClickListener ở đây nữa vì onOptionsItemSelected đã xử lý)
    }

    private fun setupRecyclerView() {
        songAdapter = SongSimpleAdapter(songList) { /* Xử lý khi nhấn vào bài hát */ }
        binding.songsRecyclerViewDetail.apply {
            layoutManager = LinearLayoutManager(this@ArtistDetailActivity)
            adapter = songAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun updateArtistInfo(artist: ArtistItem) {
        binding.artistNameDetail.text = artist.name
        try {
            binding.artistImageDetail.setImageResource(artist.imageResource)
        } catch (e: Exception) {
            binding.artistImageDetail.setImageResource(R.drawable.ic_launcher_background)
        }
        val artistSongs = createDummySongs().filter { it.artist.contains(artist.name, ignoreCase = true) }
        val totalDurationSeconds = artistSongs.sumOf { it.durationInSeconds.toInt() }
        val formattedDuration = formatDuration(totalDurationSeconds)
        binding.artistStatsDetail.text = "${artist.albumCount} Albums   |   ${artist.songCount} Songs   |   $formattedDuration"
    }

    private fun loadSongsForArtist(artistName: String?) {
        if (artistName == null) return
        val allSongs = createDummySongs()
        val artistSongs = allSongs.filter { it.artist.contains(artistName, ignoreCase = true) }
        songList.clear()
        songList.addAll(artistSongs)
        songAdapter.notifyDataSetChanged()
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()) % 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d mins", minutes, remainingSeconds)
    }

    // Dữ liệu mẫu
    private fun createDummySongs(): List<Song> {
        val defaultImage = R.drawable.ic_launcher_background
        return listOf(
            Song("s1", "Không Thời Gian", "Dương Domic", defaultImage, 210),
            Song("s2", "Tràn Bộ Nhớ", "Dương Domic", defaultImage, 195),
            Song("s3", "Pin Dự Phòng", "Dương Domic, Lou Hoàng", defaultImage, 220),
            Song("s4", "Buồn Hay Vui", "RPT MCK", defaultImage, 180),
            Song("s5", "Chìm Sâu", "RPT MCK", defaultImage, 200),
            Song("s6", "Ghé Qua", "Wvrdie", defaultImage, 240)
        )
    }
}
