package com.amatai.lybra_app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.Configuracion
import com.amatai.lybra_app.databinding.FragmentConfiguracionesBinding
import com.amatai.lybra_app.ui.activities.LoginActivity
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelConfiguracionFragment

class ConfiguracionesFragment : Fragment() {

    val viewmodel by viewModels<ViewmodelConfiguracionFragment> {
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
        val binding = FragmentConfiguracionesBinding.inflate(inflater, container, false)
        context ?: binding.root

        val switchAudio = binding.switchAudioVideo
        val switchBoton = binding.switchBotonPanico
        val switchMensajes = binding.switchMensajes
        val switchNotificaciones = binding.switchNotificacionesApp

        var grabacionVideo = false
        var botonpanico = false
        var mensajes = false
        var notificaciones = false

        binding.guardarConfiguracion.setOnClickListener {

            grabacionVideo = switchAudio.isChecked
            botonpanico = switchBoton.isChecked
            mensajes = switchMensajes.isChecked
            notificaciones = switchNotificaciones.isChecked

            var configuracion = Configuracion(
                1,
                grabacionVideo,
                botonpanico,
                mensajes,
                notificaciones
            )
            Log.d("configuracionnn", configuracion.toString())

            viewmodel.guardarConfiguracion(configuracion)
            Toast.makeText(requireContext(), "Se guardo configuracion", Toast.LENGTH_SHORT).show()

            requireActivity().finish()
            var intet = Intent(requireContext(), LoginActivity::class.java)
            requireContext().startActivity(intet)

        }

        viewmodel.obtenerConfiguracion().observe(viewLifecycleOwner, Observer {
            if (it.llavePrimaria == 1) {
                binding.guardarConfiguracion.visibility = View.GONE
                binding.actualizarConfiguracion.visibility = View.VISIBLE

                binding.switchAudioVideo.isChecked = it.grabarVideoAudio!!
                binding.switchBotonPanico.isChecked = it.botonPanico!!
                binding.switchMensajes.isChecked = it.enviarMensaje!!
                binding.switchNotificacionesApp.isChecked = it.activarNotificacion!!

                Log.d("configuracionobtenida", it.toString())
            }
        })

        binding.actualizarConfiguracion.setOnClickListener {

            grabacionVideo = switchAudio.isChecked
            botonpanico = switchBoton.isChecked
            mensajes = switchMensajes.isChecked
            notificaciones = switchNotificaciones.isChecked

            var configuracion = Configuracion(
                1,
                grabacionVideo,
                botonpanico,
                mensajes,
                notificaciones
            )
            Log.d("configuracionnn", configuracion.toString())
            viewmodel.actualizarConfiguracion(configuracion)

            Toast.makeText(requireContext(), "Se actualizo la configuracion", Toast.LENGTH_SHORT)
                .show()
            //findNavController().navigateUp()
            requireActivity().finish()
            var intet = Intent(requireContext(), LoginActivity::class.java)
            requireContext().startActivity(intet)

        }

        return binding.root
    }
}