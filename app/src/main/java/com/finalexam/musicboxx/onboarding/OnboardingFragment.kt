package com.finalexam.musicboxx.onboarding

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.finalexam.musicboxx.R

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val btnNext = view.findViewById<Button>(R.id.btnNext)

        val adapter = OnboardingAdapter()
        viewPager.adapter = adapter

        btnNext.setOnClickListener {
            val current = viewPager.currentItem
            if (current < adapter.itemCount - 1) {
                viewPager.currentItem = current + 1
            } else {
                findNavController().navigate(
                    R.id.action_onboarding_to_home
                )
            }
        }

    }
}
