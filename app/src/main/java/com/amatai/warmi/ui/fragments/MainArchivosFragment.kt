package com.amatai.warmi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.amatai.warmi.R
import com.amatai.warmi.ui.adapters.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main_archivos.*
import java.lang.Exception

class MainArchivosFragment : Fragment() {

    private lateinit var adapterPager: PagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_archivos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterPager = PagerAdapter(requireActivity())

        try {

            ViewPager2.isSaveEnabled = true

            ViewPager2.adapter = adapterPager

            TabLayoutMediator(tapLayout, ViewPager2, true){tab, position ->

                tapLayout.getTabAt(0)?.select()
                ViewPager2.setCurrentItem(0, false)

                if (position == 0){
                    tab.text = "Videos"
                }

                if (position == 1){
                    tab.text = "Audios"
                }


            }.attach()

        }catch (e:Exception){

        }
    }

}