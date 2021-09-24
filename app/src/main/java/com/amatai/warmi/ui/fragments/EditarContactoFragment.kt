package com.amatai.warmi.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.amatai.warmi.R
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databasemanager.entities.ContactosEntity
import com.amatai.warmi.databinding.FragmentEditarContactoBinding
import com.amatai.warmi.hideKeyboard
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelEditarContactoFragment

class EditarContactoFragment : Fragment() {

    lateinit var contacto:ContactosEntity

    val viewmodelEditarContactoFragmetn by viewModels<ViewmodelEditarContactoFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            contacto = it.getParcelable("contacto")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditarContactoBinding.inflate(inflater, container, false)
        context ?: return binding.root

        //Log.d("contactoeditar",contacto.toString())

        binding.nombreActualizarContacto.setText(contacto.name)
        binding.correoActualizarContacto.setText(contacto.email)
        binding.telefonoActualizarContacto.setText(contacto.number_phone)
        binding.direccionActualizarContacto.setText(contacto.adress)

        if (contacto.is_trusted == 1){
            binding.contactoConfianzaActualizar.isChecked = true
        }

        binding.actualizarContactoboton.setOnClickListener {
            if (!binding.contactoConfianzaActualizar.isChecked){
                contacto.is_trusted = 0
            }else{
                contacto.is_trusted = 1
            }

            val nombreActualizado = binding.nombreActualizarContacto.text.toString()
            val emailActualizado = binding.correoActualizarContacto.text.toString()
            val numeroActualizado = binding.telefonoActualizarContacto.text.toString()
            val direccionActualizada = binding.direccionActualizarContacto.text.toString()

            val contactoActualizado = ContactosEntity(
                contacto.llavePrimariaLocal,
                2,
                contacto.id,
                nombreActualizado,
                emailActualizado,
                numeroActualizado,
                direccionActualizada,
                contacto.is_trusted,
                contacto.type_status_id,
                contacto.user_id,
                contacto.created_at,
                contacto.updated_at
            )

            viewmodelEditarContactoFragmetn.actualizarContacto(contactoActualizado)
           requireActivity().hideKeyboard(requireActivity())
            it.findNavController().navigateUp()

            //Log.d("yanoesdeconfianza", contactoActualizado.toString())
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menudirectorio, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.eliminarContacto -> {
                val contactoEliminar = ContactosEntity(
                    contacto.llavePrimariaLocal,
                    3,
                    contacto.id,
                    contacto.name,
                    contacto.email,
                    contacto.number_phone,
                    contacto.adress,
                    contacto.is_trusted,
                    contacto.type_status_id,
                    contacto.user_id,
                    contacto.created_at,
                    contacto.updated_at
                )
                viewmodelEditarContactoFragmetn.actualizarContacto(contactoEliminar)
                requireActivity()
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}