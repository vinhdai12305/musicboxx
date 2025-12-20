package com.finalexam.musicboxx.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.finalexam.musicboxx.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. LẤY BottomNavigationView từ layout
        val bottomNav =
            view.findViewById<BottomNavigationView>(R.id.bottomNav)

        // 2. LẤY NavHostFragment con (main_nav_host)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.main_nav_host)
                    as NavHostFragment

        // 3. KẾT NỐI BottomNav với NavController
        bottomNav.setupWithNavController(navHostFragment.navController)
    }
}