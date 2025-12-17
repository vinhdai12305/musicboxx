package com.finalexam.musicboxx.splash

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.finalexam.musicboxx.R


class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivLoading = view.findViewById<ImageView>(R.id.ivLoading)

        // Chạy animation nếu có
        try {
            val rotateAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_loading)
            ivLoading.startAnimation(rotateAnim)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Delay 3s rồi sang OnboardingFragment
        view.postDelayed({
            findNavController().navigate(
                R.id.action_splashFragment_to_onboardingFragment
            )
        }, 3000)
    }
}
