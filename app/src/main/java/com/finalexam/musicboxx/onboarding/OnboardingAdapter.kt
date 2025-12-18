package com.finalexam.musicboxx.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R

class OnboardingAdapter :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val layouts = listOf(
        R.layout.item_onboarding_1,
        R.layout.item_onboarding_2,
        R.layout.item_onboarding_3
    )

    inner class OnboardingViewHolder(view: View) :
        RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layouts[viewType], parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int
    ) {
        // KHÔNG LÀM GÌ Ở ĐÂY
        // Adapter chỉ hiển thị UI
    }

    override fun getItemCount(): Int = layouts.size

    override fun getItemViewType(position: Int): Int = position
}
