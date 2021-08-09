package com.amatai.lybra_app.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amatai.lybra_app.R
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.data.repositories.RepositoryImpl
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.Configuracion
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.viewmodels.VMFactory
import com.amatai.lybra_app.ui.viewmodels.ViewmodelLogin
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_login_fragment.*

class LoginFragment : Fragment() {

    val viewmodelLogin by viewModels<ViewmodelLogin> { VMFactory(RepositoryImpl(DataSources(
        AppDatabase.getDatabase(requireContext())!!))) }
    val jsonDataLogin = JsonObject()


    private val REQUIRED_PERMISSIONS =
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.SEND_SMS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE
        )

    private val REQUEST_CODE_PERMISSIONS = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodelLogin.obtenerConfiguracion.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it == null){

                val configuracion = Configuracion(
                    1,
                    true,
                    true,
                    true,
                    true,
                    false
                )

                viewmodelLogin.guardarConfiguracion(configuracion)

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodelLogin.logueo.observe(viewLifecycleOwner, Observer {
            if (it){

                AlertDialog.Builder(requireContext())
                    .setMessage("Autenticacion incorrecta")
                    .setPositiveButton("Aceptar"){dialog, which ->
                        dialog.dismiss()
                    }.show()
            }
        })


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        viewmodelLogin.obtenerUsuarioLogueado().observe(viewLifecycleOwner, Observer {
            if (it != null){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        })

        botonRegistrarse.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url","https://recepciondecasos.corporacionochodemarzo.org/register")
            findNavController().navigate(R.id.action_loginFragmentFragment_to_registrarseFragment, bundle)
        }
        video.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url","https://tutoriales.corporacionochodemarzo.org/tutorialapiindex.php")
            findNavController().navigate(R.id.action_loginFragmentFragment_to_registrarseFragment, bundle)
        }

        botonLogin.setOnClickListener {

            if (!nombreUsuario.text.isNullOrEmpty() && !claveUsuario.text.isNullOrEmpty()) {
                jsonDataLogin.addProperty("email", nombreUsuario.text.toString())
                jsonDataLogin.addProperty("password", claveUsuario.text.toString())
                //Log.d("datos", nombreUsuario.text.toString())

                viewmodelLogin.loguin(jsonDataLogin).observe(viewLifecycleOwner, Observer {

                    if (it.access_token != "") {

                        val sessionLogueo = SessionLogueo(null, access_token = it.access_token,
                        token_type = it.token_type, expires_in = it.expires_in)

                        viewmodelLogin.agregarUsuarioLogueado(it.user)
                        viewmodelLogin.guardarSession(sessionLogueo)

                        var intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                })
                Toast.makeText(requireActivity(), "Logueando", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Ingrese Su Usuario y/o Contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {

            } else {
                Toast.makeText(requireContext(),
                    "No diste permisos",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}