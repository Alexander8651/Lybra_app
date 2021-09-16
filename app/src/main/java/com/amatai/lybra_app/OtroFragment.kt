package com.amatai.lybra_app

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import kotlinx.android.synthetic.main.fragment_otro.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OtroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OtroFragment : Fragment() {
    val viewmodelLogin by viewModels<ViewmodelLogin> { VMFactory(RepositoryImpl(DataSources(
        AppDatabase.getDatabase(requireContext())!!))) }
    val jsonDataLogin = JsonObject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        botonPolihome.setOnClickListener {

            findNavController().navigate(R.id.action_otroFragment_to_loginFragmentFragment)
        }

        viewmodelLogin.obtenerUsuarioLogueado().observe(viewLifecycleOwner, Observer {
            if (it != null){
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        })

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OtroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OtroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}