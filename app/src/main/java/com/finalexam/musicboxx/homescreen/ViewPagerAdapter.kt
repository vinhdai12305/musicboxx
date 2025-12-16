package com.finalexam.musicboxx.homescreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
// SỬA LẠI ĐƯỜNG DẪN IMPORT
import com.finalexam.musicboxx.homescreen.ArtistsFragment // File này có vẻ đã đúng vị trí
// CÁC FILE NÀY CÓ THỂ ĐANG NẰM TRONG `homescreen`
import com.finalexam.musicboxx.homescreen.PlaceholderFragment
import com.finalexam.musicboxx.homescreen.SongsFragment
import com.finalexam.musicboxx.homescreen.SuggestedFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5 // Suggested, Songs, Artists, Albums, Favorites

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SuggestedFragment()
            1 -> SongsFragment()
            // SỬA LẠI ĐỂ HIỂN THỊ DANH SÁCH NGHỆ SĨ
            2 -> ArtistsFragment()
            // Các tab khác vẫn dùng Fragment giữ chỗ
            3 -> PlaceholderFragment.newInstance("Albums")
            4 -> PlaceholderFragment.newInstance("Favorites")
            else -> Fragment() // Tránh bị crash
        }
    }
}
