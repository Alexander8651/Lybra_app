package com.amatai.lybra_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databinding.FragmentDirectorioBinding
import com.amatai.lybra_app.ui.adapters.ContactosAdapter
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelDirectorioFragment

class DirectorioFragment : Fragment() {

    val viewmodelDirectorioFragment by viewModels<ViewmodelDirectorioFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }

    lateinit var bindingDirectorioFragment: FragmentDirectorioBinding
    lateinit var adapterDirectorio:ContactosAdapter

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
        return inflater.inflate(R.layout.fragment_directorio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingDirectorioFragment = FragmentDirectorioBinding.bind(view)

        adapterDirectorio = ContactosAdapter()

        viewmodelDirectorioFragment.obtenerDirecotioSqlite().asLiveData().observe(viewLifecycleOwner, Observer {
            bindingDirectorioFragment.rvDirectorio.apply {
                adapter = adapterDirectorio
            }
            adapterDirectorio.submitList(it)
        })

        bindingDirectorioFragment.agregarContacto.setOnClickListener {
            it.findNavController().navigate(R.id.action_personasConfianza_to_agragarContactoFragment)
        }




    }
}