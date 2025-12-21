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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import Song // Đảm bảo import đúng model Song của bạn
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Normalizer
import java.util.regex.Pattern

class SearchFragment : Fragment(R.layout.fragment_search) {

    // Sử dụng SearchAdapter (Adapter mới bạn vừa tạo) thay vì SongsAdapter
    private lateinit var searchAdapter: SearchAdapter

    private val allSongsList = ArrayList<Song>()
    private val filteredList = ArrayList<Song>()
    private lateinit var edtSearch: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View (Đảm bảo ID khớp với fragment_search.xml)
        edtSearch = view.findViewById(R.id.edtSearchInput)
        val rvResults = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        val ivClear = view.findViewById<ImageView>(R.id.ivClear)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)

        // 2. Xử lý nút Back
        ivBack.setOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }

        // 3. Setup Adapter (Dùng SearchAdapter mới)
        searchAdapter = SearchAdapter(filteredList) { song ->
            // Khi chọn bài hát: Ẩn bàn phím & Phát nhạc
            hideKeyboard()
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        rvResults.adapter = searchAdapter
        // QUAN TRỌNG: LayoutManager để xếp dọc (Vertical) cho layout mới
        rvResults.layoutManager = LinearLayoutManager(context)

        // 4. Tải dữ liệu từ Firebase
        fetchAllSongs()

        // 5. Xử lý gõ chữ (Real-time Search)
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filterSongs(query)
                // Hiện nút X khi có chữ
                ivClear.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 6. Xử lý nút Xóa text
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
            // Khi rỗng thì không hiện gì cả (hoặc bạn có thể hiện lịch sử tìm kiếm)
        } else {
            for (song in allSongsList) {
                val normalizedTitle = removeAccents(song.title)
                val normalizedArtist = removeAccents(song.artist)

                // Tìm kiếm tương đối
                if (normalizedTitle.contains(normalizedQuery) || normalizedArtist.contains(normalizedQuery)) {
                    filteredList.add(song)
                }
            }
        }
        // Cập nhật lại giao diện bằng hàm updateData của SearchAdapter
        searchAdapter.updateData(filteredList)
    }

    // Hàm bỏ dấu tiếng Việt chuẩn
    private fun removeAccents(str: String?): String {
        if (str == null) return ""
        try {
            val temp = Normalizer.normalize(str, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
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