package com.omkarcodes.moneyx.ui.auth.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.omkarcodes.moneyx.databinding.ItemOnboardingBinding
import com.omkarcodes.moneyx.ui.auth.OnBoarding

class OnboardingAdapter(
    private val list: List<OnBoarding> = listOf(OnBoarding.Control,OnBoarding.Know,OnBoarding.Plan)
) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemOnboardingBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val onboarding = list[position]
            binding.apply {
                ivIllustration.setImageResource(onboarding.src)
                tvHeading.text = onboarding.title
                tvSubtitle.text = onboarding.subtitle
            }
        }
    }
}