package com.amatai.warmi.ui.fragments

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
import com.amatai.warmi.data.DataSources
import com.amatai.warmi.data.repositories.RepositoryImpl
import com.amatai.warmi.databasemanager.AppDatabase
import com.amatai.warmi.databasemanager.entities.Configuracion
import com.amatai.warmi.databinding.FragmentConfiguracionesBinding
import com.amatai.warmi.ui.activities.LoginActivity
import com.amatai.warmi.ui.viewmodels.VMFactory
import com.amatai.warmi.ui.viewmodels.ViewmodelConfiguracionFragment

class ConfiguracionesFragment : Fragment() {

    val viewmodel by viewModels<ViewmodelConfiguracionFragment> {
        VMFactory(RepositoryImpl(DataSources(AppDatabase.getDatabase(requireContext())!!)))
    }


    var primerInicio = true

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
        val switchServicio = binding.switchServicioApp

        var grabacionVideo = false
        var botonpanico = false
        var mensajes = false
        var notificaciones = false
        var servicio = false

        binding.guardarConfiguracion.setOnClickListener {

            grabacionVideo = switchAudio.isChecked
            botonpanico = switchBoton.isChecked
            mensajes = switchMensajes.isChecked
            notificaciones = switchNotificaciones.isChecked
            servicio = switchServicio.isChecked

            var configuracion = Configuracion(
                1,
                grabacionVideo,
                botonpanico,
                mensajes,
                notificaciones,
                true
            )
            Log.d("configuracionnn", servicio.toString())

            viewmodel.actualizarConfiguracion(configuracion)
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
                binding.switchServicioApp.isChecked = it.activarBotonFisico!!

                Log.d("configuracionobtenida", it.toString())
            }
        })

        binding.actualizarConfiguracion.setOnClickListener {

            grabacionVideo = switchAudio.isChecked
            botonpanico = switchBoton.isChecked
            mensajes = switchMensajes.isChecked
            notificaciones = switchNotificaciones.isChecked
            servicio = switchServicio.isChecked

            var configuracion = Configuracion(
                1,
                grabacionVideo,
                botonpanico,
                mensajes,
                notificaciones,
                servicio
            )
            Log.d("configuracionnn", configuracion.toString())
            viewmodel.actualizarConfiguracion(configuracion)

            Toast.makeText(requireContext(), "Se actualizo la configuracion", Toast.LENGTH_SHORT)
                .show()
            requireActivity().finish()
            var intet = Intent(requireContext(), LoginActivity::class.java)
            requireContext().startActivity(intet)

        }

        return binding.root
    }
}