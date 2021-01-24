package com.amatai.lybra_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.MediaController
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databinding.FragmentArchivosBinding
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.adapters.VideoAdapter
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelArchivosFragment
import kotlinx.android.synthetic.main.fragment_archivos.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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