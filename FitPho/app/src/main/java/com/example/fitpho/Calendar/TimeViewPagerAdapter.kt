package com.example.fitpho.Calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.jjobes.slidedatetimepicker.DateFragment
import com.github.jjobes.slidedatetimepicker.TimeFragment

class TimeViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> DateFragment()
            1-> TimeFragment()
            else -> Fragment()
        }
    }

}