package com.finalexam.musicboxx.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R

class FolderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_folder, container, false)

        // 1. Dữ liệu giả giống hình
        val folders = listOf(
            Folder(1, "Top 100 Billboards", "100 Songs"),
            Folder(2, "My Favorite Songs", "240 Songs"),
            Folder(3, "Most Popular Songs", "44 Songs"),
            Folder(4, "K-Pop Hits", "50 Songs"),
            Folder(5, "V-Pop 2025", "20 Songs"),
            Folder(6, "Chill Lofi", "15 Songs")
        )

        // 2. Setup RecyclerView
        val rvFolders = view.findViewById<RecyclerView>(R.id.rvFolders)
        rvFolders.layoutManager = LinearLayoutManager(context) // Danh sách dọc
        rvFolders.adapter = FolderAdapter(folders)

        return view
    }
}