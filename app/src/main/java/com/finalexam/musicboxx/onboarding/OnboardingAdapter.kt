package com.finalexam.musicboxx.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.finalexam.musicboxx.R

class OnboardingAdapter :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val layouts = listOf(
        R.layout.item_onboarding_1,
        R.layout.item_onboarding_2,
        R.layout.item_onboarding_3
    )

    inner class OnboardingViewHolder(val view: View) :
        RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layouts[viewType], parent, false)
        return OnboardingViewHolder(view)
    }

    override fun getItemCount(): Int = layouts.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int
    ) {
        val btnNext = holder.view.findViewById<Button>(R.id.btnNext)

        btnNext.setOnClickListener {
            val viewPager =
                holder.itemView.parent.parent as ViewPager2

            if (position < layouts.size - 1) {
                viewPager.currentItem = position + 1
            } else {
                // TODO: chuyển sang Login / Home
            }
        }
    }
}
