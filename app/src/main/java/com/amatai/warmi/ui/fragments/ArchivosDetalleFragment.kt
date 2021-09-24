package com.amatai.warmi.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.amatai.warmi.R
import com.amatai.warmi.databasemanager.entities.VideoEntity
import com.amatai.warmi.databinding.FragmentArchivosDetalleBinding
import com.amatai.warmi.ui.activities.MainActivity

class ArchivosDetalleFragment : Fragment() {

    lateinit var video:VideoEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            video = it.getParcelable("archivo")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentArchivosDetalleBinding.inflate(inflater, container, false)
        context ?: binding.root

        /*
        val root = "storage/emulated/0/videoslybra"
        File(root).walkTopDown().forEachIndexed{ index, file ->
            if (index == 9){
                Log.d("archivosss", file.path)
                val mediaController = MediaController(requireContext())
                mediaController.setAnchorView(binding.videoplayerrr)
                binding.videoplayerrr.setMediaController(mediaController)
                binding.videoplayerrr.setVideoPath(file.path)
                binding.videoplayerrr.start()
            }
        }
        */

        (activity as MainActivity).supportActionBar!!.hide()
        requireActivity().window.statusBarColor = Color.TRANSPARENT

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoplayerrr)
        binding.videoplayerrr.setMediaController(mediaController)
        binding.videoplayerrr.setVideoPath(video.path)
        binding.videoplayerrr.start()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().window.statusBarColor =  resources.getColor(R.color.colorPrimary)
        (activity as MainActivity).supportActionBar!!.show()

    }
}