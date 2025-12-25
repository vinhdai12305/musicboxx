package com.finalexam.musicboxx.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.finalexam.musicboxx.hometab.AlbumsFragment
import com.finalexam.musicboxx.hometab.ArtistsFragmentHomeTab
import com.finalexam.musicboxx.hometab.SongsFragment
import com.finalexam.musicboxx.home.SuggestedFragment
class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SuggestedFragment() // Màn hình Suggested (Code cũ)
            1 -> SongsFragment()     // Màn hình Songs (Mới)
            2 -> ArtistsFragmentHomeTab() // Màn hình Artists (Mới)
            3 -> AlbumsFragment()    // Màn hình Albums (Mới)
            else -> SuggestedFragment()
        }
    }
}