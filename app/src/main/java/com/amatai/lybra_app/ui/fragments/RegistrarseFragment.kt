package com.amatai.lybra_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.amatai.lybra_app.R
import kotlinx.android.synthetic.main.fragment_registrarse.*


class RegistrarseFragment : Fragment() {


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
        return inflater.inflate(R.layout.fragment_registrarse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerVinculo()
    }
    //se despliega el spinner del documento
    fun spinnerVinculo() {

        // Se declara spinner con rescursos string y su adapter
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipoDocumento,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipoDocumentoRegistarse!!.adapter = adapter
        }
    }


}