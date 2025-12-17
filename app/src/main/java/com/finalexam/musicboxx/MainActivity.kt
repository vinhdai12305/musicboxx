package com.finalexam.musicboxx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.finalexam.musicboxx.homescreen.HomeTabController
import com.finalexam.musicboxx.homescreen.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), HomeTabController {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottom_navigation)

        // ✅ DÙNG DUY NHẤT ViewPagerAdapter
        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Suggested"
                1 -> "Songs"
                2 -> "Artists"
                3 -> "Albums"
                else -> "Favorites"
            }
        }.attach()

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.nav_favorites -> {
                    viewPager.currentItem = 4
                    true
                }
                else -> false
            }
        }
    }

    // 👉 ĐƯỢC GỌI TỪ SuggestedFragment
    override fun goToSongsTab() {
        viewPager.currentItem = 1
    }
}
