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
import com.amatai.warmi.databinding.FragmentNotificacionesBinding
import com.amatai.warmi.ui.adapters.NotificacionesAdapter
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelNotificacionesFragment

class NotificacionesFragment : Fragment() {

    val viewmodelNotificacionesFragment by viewModels<ViewmodelNotificacionesFragment> {VMFactory(RepositoryImpl(
        DataSources(AppDatabase.getDatabase(requireContext())!!)
    ))  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNotificacionesBinding.inflate(inflater,  container, false)

        context ?: binding.root

        binding.viewModel = viewmodelNotificacionesFragment

        viewmodelNotificacionesFragment.obtenerNotificaciones().observe(viewLifecycleOwner, Observer {
            val adapterNotificaciones = NotificacionesAdapter()
            binding.rvNotificaciones.apply {

                adapter = adapterNotificaciones
                adapterNotificaciones.submitList(it)
                binding.statusImage.visibility = View.GONE
            }
        })


        return binding.root
    }

}