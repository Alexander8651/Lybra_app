package com.amatai.warmi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databinding.FragmentArchivosBinding
import com.amatai.warmi.ui.adapters.VideoAdapter
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelArchivosFragment
import kotlinx.android.synthetic.main.fragment_archivos.*

class ArchivosFragment : Fragment() {

    val viewmodelArchivosFragment by viewModels<ViewmodelArchivosFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }

    lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentArchivosBinding.inflate(inflater, container, false)
        context ?: binding.root

        videoAdapter = VideoAdapter()

        /*
        val root = "storage/emulated/0/videoslybra"
        File(root).walkTopDown().forEach {
            when {
                it.isFile -> {
                    Log.d("archivos", it.toString())
                }
            }
        }
         */


        viewmodelArchivosFragment.obtenerVideosSqlite().observe(viewLifecycleOwner, Observer {
            binding.rvVideos.apply {
                adapter = videoAdapter
            }

            videoAdapter.submitList(it)
        })

        return binding.root
    }
}