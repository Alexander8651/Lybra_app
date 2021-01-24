package com.amatai.lybra_app.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.amatai.lybra_app.databinding.FragmentReporteEnviadosBinding
import com.amatai.lybra_app.ui.adapters.ReporteAdapter
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelReporteFragment

class ReporteEnviadosFragment : Fragment() {

    val viewmoReporteEnviadosFragment by viewModels<ViewmodelReporteFragment> {
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

        val binding = FragmentReporteEnviadosBinding.inflate(inflater, container, false)
        context ?: binding.root



        viewmoReporteEnviadosFragment.reportes.observe(viewLifecycleOwner, Observer {
            val reporteAdapter = ReporteAdapter()

            binding.rvReportes.apply {
                adapter = reporteAdapter
            }

            reporteAdapter.submitList(it.sortedBy {list ->
                list.created_rg
            })
            Log.d("repostesss", it.toString())
        })


        return binding.root
    }
}