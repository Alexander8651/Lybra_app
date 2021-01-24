package com.amatai.lybra_app.ui.adapters

import android.os.Parcelable
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.amatai.lybra_app.ui.fragments.ArchivosFragment
import com.amatai.lybra_app.ui.fragments.AudioFragment


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
