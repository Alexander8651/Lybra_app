package com.amatai.warmi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.amatai.warmi.R
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databinding.FragmentDirectorioBinding
import com.amatai.warmi.ui.adapters.ContactosAdapter
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelDirectorioFragment

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

        viewmodelDirectorioFragment.obtenerDirecotioSqlite().observe(viewLifecycleOwner, Observer {
            bindingDirectorioFragment.rvDirectorio.apply {
                adapter = adapterDirectorio
            }
            adapterDirectorio.submitList(it)
            Log.d("directorioaaopeprprpfgk", it.toString())
        })

        bindingDirectorioFragment.agregarContacto.setOnClickListener {
            it.findNavController().navigate(R.id.action_personasConfianza_to_agragarContactoFragment)
        }

    }


}

