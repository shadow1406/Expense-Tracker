package com.prathmesh.moneyx.ui.home.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.prathmesh.moneyx.ui.home.fragments.HomeFragment
import com.prathmesh.moneyx.ui.home.fragments.TransactionsFragment

class MainViewpagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) HomeFragment() else TransactionsFragment()
    }
}