package com.amatai.warmi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amatai.warmi.R
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.ui.adapters.AudioAdapter
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelAudioFragment
import kotlinx.android.synthetic.main.fragment_audio.*
class AudioFragment : Fragment() {

    val viewmodelAudio by viewModels<ViewmodelAudioFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }

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
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val adapterAudio = AudioAdapter()

        viewmodelAudio.obtenerAudios().observe(viewLifecycleOwner, Observer {
            rvAudio.apply {
                adapter = adapterAudio
            }
            adapterAudio.submitList(it)
        })



    }

}