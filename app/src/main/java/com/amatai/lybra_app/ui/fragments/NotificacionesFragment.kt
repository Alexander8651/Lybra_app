package com.amatai.lybra_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databinding.FragmentNotificacionesBinding
import com.amatai.lybra_app.ui.adapters.ApiStatus
import com.amatai.lybra_app.ui.adapters.NotificacionesAdapter
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelNotificacionesFragment

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