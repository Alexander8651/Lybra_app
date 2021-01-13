package com.amatai.lybra_app.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.UsuarioRegistro
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databinding.FragmentRegistrarseBinding
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelRegistrarseFragment


class RegistrarseFragment : Fragment() {

    lateinit var binding: FragmentRegistrarseBinding
    var tipoDocumento = 0
    val viewmodelRegistrarseFragment by viewModels<ViewmodelRegistrarseFragment> {
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

        binding = FragmentRegistrarseBinding.inflate(inflater, container, false)
        context ?: binding.root

        spinnerTipoDocumento()
        tipoDocumento()

        val nombres = binding.nombreRegistro
        val correo = binding.correoRegistro
        val telefono = binding.telefonoRegistro
        val direccion = binding.direccionRegistro
        val numeroDocumento = binding.numeroDocumentoRegistro
        val contrasena = binding.contrasenaRegistro



        binding.registrarmeboton.setOnClickListener {
            Log.d("tipodocumento", tipoDocumento.toString())

            if (tipoDocumento == 0) {
                Toast.makeText(
                    requireContext(),
                    "Selecciona el tipo de documento",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!nombres.text.isNullOrEmpty() && !correo.text.isNullOrEmpty() && !telefono.text.isNullOrEmpty()
                    && !direccion.text.isNullOrEmpty() && !numeroDocumento.text.isNullOrEmpty() && !contrasena.text.isNullOrEmpty()
                ) {

                    val usuarioRegistro = UsuarioRegistro(
                        nombres.text.toString(),
                        correo.text.toString(),
                        telefono.text.toString(),
                        direccion.text.toString(),
                        numeroDocumento.text.toString(),
                        contrasena.text.toString(),
                        tipoDocumento
                    )
                    Log.d("registrar", usuarioRegistro.toString())
                    viewmodelRegistrarseFragment.registrarNuevoUsuario(usuarioRegistro).observe(viewLifecycleOwner, Observer {
                        if (it.email == null){
                            AlertDialog.Builder(requireContext())
                                .setMessage("Este correo ya esta en uso")
                                .setPositiveButton("Aceptar"){dialog, which ->
                                    dialog.dismiss()
                                }.show()

                        }else{
                            findNavController().navigateUp()
                            Toast.makeText(requireContext(), "Se creo un nuevo usuario", Toast.LENGTH_SHORT).show()
                        }
                    })

                } else {

                    Toast.makeText(
                        requireContext(),
                        "Ingrese todos los datos solicitados",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        return binding.root
    }

    //se despliega el spinner del documento
    fun spinnerTipoDocumento() {

        // Se declara spinner con rescursos string y su adapter
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipoDocumento,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.tipoDocumentoRegistarse.adapter = adapter
        }
    }

    fun tipoDocumento() {
        //Spinner del vindculo
        binding.tipoDocumentoRegistarse.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val vinculo = parent!!.getItemAtPosition(p2).toString()
                    //Asigna los valores al vinduclo 1,2 o 3
                    when (vinculo) {
                        "Selecciona el tipo de documento" -> tipoDocumento = 0
                        "CC" -> tipoDocumento = 6
                        "Nit" -> tipoDocumento = 5
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }


}