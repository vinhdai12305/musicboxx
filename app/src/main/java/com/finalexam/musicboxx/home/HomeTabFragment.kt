package com.finalexam.musicboxx.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.finalexam.musicboxx.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// File này chỉ quản lý Tab, không load nhạc
class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Xử lý nút Search
        view.findViewById<View>(R.id.ivSearch).setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        // 2. Setup ViewPager và Tab
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        // Gắn Adapter
        val adapter = HomeViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Đặt tên Tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Suggested"
                1 -> tab.text = "Songs"
                2 -> tab.text = "Artists"
                3 -> tab.text = "Albums"
            }
        }.attach()
    }
}