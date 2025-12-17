package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.finalexam.musicboxx.R

class SongsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs_list)

        setupTabs()
    }

    private fun setupTabs() {
        // ĐỂ TRỐNG – sẽ làm sau bằng ViewPager2
    }
}
