package com.amatai.lybra_app.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databinding.FragmentAgragarContactoBinding
import com.amatai.lybra_app.hideKeyboard
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelAgregarContactoFragment
import kotlinx.android.synthetic.main.fragment_login_fragment.view.*

class AgragarContactoFragment : Fragment() {

    val viewmodelAgregarContactoFragmetn by viewModels<ViewmodelAgregarContactoFragment> {
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
        val binding = FragmentAgragarContactoBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val nombreContacto = binding.nombreContacto
        val correoContacto = binding.correoContacto
        val telefonoContacto = binding.telefonoContacto
        val direccionContacto = binding.direccionContacto
        val contactoConfianza =binding.contactoConfianza

        binding.agregarContactoboton.setOnClickListener {
            if (!nombreContacto.text.isNullOrEmpty() && !correoContacto.text.isNullOrEmpty() && !telefonoContacto.text.isNullOrEmpty()
                && !direccionContacto.text.isNullOrEmpty()){

                val contactosEntity= ContactosEntity(
                    null,
                    1,
                    0,
                    nombreContacto.text.toString(),
                    correoContacto.text.toString(),
                    telefonoContacto.text.toString(),
                    direccionContacto.text.toString(),  0, 0, 0, "", "")

                if (contactoConfianza.isChecked){
                    contactosEntity.is_trusted = 1
                }else{
                    contactosEntity.is_trusted  = 0
                }

                viewmodelAgregarContactoFragmetn.agregarContactoSqlite(contactosEntity)
                requireActivity().hideKeyboard(requireActivity())
                findNavController().navigateUp()

            }else{
                Toast.makeText(requireContext(), "Debes agregar todos los campos del contacto", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }


}