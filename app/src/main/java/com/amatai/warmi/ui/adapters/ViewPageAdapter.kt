package com.amatai.warmi.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.amatai.warmi.ui.fragments.ArchivosFragment
import com.amatai.warmi.ui.fragments.AudioFragment


class PagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return  when(position){
            0 -> {
                ArchivosFragment()
            }
            1 -> {
                AudioFragment()
            }
            else -> {ArchivosFragment()
            }
        }
    }
}
