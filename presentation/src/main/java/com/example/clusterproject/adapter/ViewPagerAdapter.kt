package com.example.clusterproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.clusterproject.view.BleFragment
import com.example.clusterproject.view.IpcFragment
import com.example.clusterproject.view.HomeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> BleFragment()
            2 -> IpcFragment()
            else -> throw IllegalArgumentException("Invalid Position")
        }
    }
}