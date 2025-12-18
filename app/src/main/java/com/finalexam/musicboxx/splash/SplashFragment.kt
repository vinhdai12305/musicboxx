package com.finalexam.musicboxx.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.finalexam.musicboxx.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var loadingAnim: Animation? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivLoading = view.findViewById<ImageView>(R.id.ivLoading)

        // Load & start animation
        loadingAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_loading)
        ivLoading.startAnimation(loadingAnim)

        // Delay 3s rồi chuyển sang Onboarding
        handler.postDelayed({
            // Kiểm tra fragment còn tồn tại & đúng destination
            if (isAdded && findNavController().currentDestination?.id == R.id.splashFragment) {
                findNavController().navigate(
                    R.id.action_splash_to_onboarding
                )
            }
        }, 3000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Huỷ animation & callback để tránh leak / crash
        loadingAnim?.cancel()
        handler.removeCallbacksAndMessages(null)
    }
}
