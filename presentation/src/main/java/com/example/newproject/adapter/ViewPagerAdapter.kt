package com.example.newproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newproject.view.BleFragment
import com.example.newproject.view.ContactFragment
import com.example.newproject.view.IpcFragment
import com.example.newproject.view.NewsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> ContactFragment()
            2 -> BleFragment()
            3 -> IpcFragment()
            else -> throw IllegalArgumentException("Invalid Position")
        }
    }
}