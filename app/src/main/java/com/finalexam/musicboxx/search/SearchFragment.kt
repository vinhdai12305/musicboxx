package com.finalexam.musicboxx.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // QUAN TRỌNG: Import để dùng nút Back
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.home.SongsAdapter
import Song // Kiểm tra lại dòng này cho khớp với model Song của bạn
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Normalizer
import java.util.regex.Pattern

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchAdapter: SongsAdapter
    private val allSongsList = ArrayList<Song>()
    private val filteredList = ArrayList<Song>()
    private lateinit var edtSearch: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View
        edtSearch = view.findViewById(R.id.edtSearchInput)
        val rvResults = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        val ivClear = view.findViewById<ImageView>(R.id.ivClear)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack) // Ánh xạ nút Back

        // 2. Xử lý nút Back (QUAN TRỌNG)
        ivBack.setOnClickListener {
            // Ẩn bàn phím trước khi thoát cho đỡ lỗi giao diện
            hideKeyboard()
            // Quay lại màn hình trước đó
            findNavController().popBackStack()
        }

        // 3. Setup Adapter
        searchAdapter = SongsAdapter(filteredList) { song ->
            // Ẩn bàn phím khi chọn bài hát
            hideKeyboard()
            // Phát nhạc
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }
        rvResults.adapter = searchAdapter
        rvResults.layoutManager = LinearLayoutManager(context)

        // 4. Tải dữ liệu
        fetchAllSongs()

        // 5. Xử lý gõ chữ
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filterSongs(query)
                // Hiện nút X khi có chữ, ẩn khi rỗng
                ivClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 6. Xử lý nút Xóa text (dấu X)
        ivClear.setOnClickListener {
            edtSearch.setText("")
            showKeyboard() // Xóa xong vẫn giữ bàn phím để gõ tiếp
        }

        // Bấm vào ô search thì hiện bàn phím
        edtSearch.setOnClickListener { showKeyboard() }
    }

    // --- CÁC HÀM HỖ TRỢ ---

    override fun onResume() {
        super.onResume()
        // Khi màn hình hiện lên thì bật bàn phím ngay
        edtSearch.postDelayed({ showKeyboard() }, 200)
    }

    private fun showKeyboard() {
        edtSearch.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun filterSongs(query: String) {
        filteredList.clear()
        val normalizedQuery = removeAccents(query)

        if (normalizedQuery.isEmpty()) {
            // Khi ô tìm kiếm rỗng: Có thể để trống hoặc hiện bài gợi ý tùy ý bạn
        } else {
            for (song in allSongsList) {
                val normalizedTitle = removeAccents(song.title)
                val normalizedArtist = removeAccents(song.artist)

                // Tìm kiếm tương đối (chứa từ khóa)
                if (normalizedTitle.contains(normalizedQuery) || normalizedArtist.contains(normalizedQuery)) {
                    filteredList.add(song)
                }
            }
        }
        searchAdapter.notifyDataSetChanged()
    }

    // Hàm bỏ dấu tiếng Việt (mạnh mẽ hơn)
    private fun removeAccents(str: String?): String {
        if (str == null) return ""
        try {
            val temp = Normalizer.normalize(str, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            // Chuyển d -> d để tìm kiếm chính xác hơn
            return pattern.matcher(temp).replaceAll("").lowercase().replace("đ", "d")
        } catch (e: Exception) {
            return str.lowercase()
        }
    }

    private fun fetchAllSongs() {
        FirebaseFirestore.getInstance().collection("songs")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Song::class.java)
                    allSongsList.clear()
                    allSongsList.addAll(list)
                }
            }
    }
}