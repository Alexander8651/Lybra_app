package com.amatai.lybra_app.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelSalirFragment

class SalirFragment : Fragment() {

    val viewmodelSalirFragment by viewModels<ViewmodelSalirFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_salir, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var usuarioLogueado:UsuarioLogueado? = null
        var sessionLogueada:SessionLogueo? = null
        var directorio:List<ContactosEntity>? = null

        viewmodelSalirFragment.obtenerUsuarioLogueado.observe(viewLifecycleOwner, Observer {
            usuarioLogueado = it
        })
        viewmodelSalirFragment.obtenerSessionLogueada.observe(viewLifecycleOwner, Observer {
            sessionLogueada = it
        })

        viewmodelSalirFragment.obtenerDirectorio.observe(viewLifecycleOwner, Observer {
            directorio = it
        })

        AlertDialog.Builder(requireContext())
            .setMessage("¿Deseas Cerrar la sesion?")
            .setPositiveButton("Cerrar Session"){dialog, which ->

                if (usuarioLogueado != null){
                    viewmodelSalirFragment.borrarUsuarioLogueado(usuarioLogueado!!)
                }
                if (sessionLogueada != null){
                    viewmodelSalirFragment.borrarSessionLogueada(sessionLogueada!!)
                }
                if (directorio != null){
                    viewmodelSalirFragment.borrarDirectorio(directorio!!)
                }

                viewmodelSalirFragment.borrarReportes()
                requireActivity().finish()
            }
            .setNegativeButton("Cancelar"){dialog, which ->
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()

    }
}