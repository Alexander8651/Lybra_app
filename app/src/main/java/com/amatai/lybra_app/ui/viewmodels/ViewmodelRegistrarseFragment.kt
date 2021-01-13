package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.UsuarioRegistro
import com.amatai.lybra_app.data.repositories.Repository
import com.google.gson.JsonObject
import java.lang.Exception

class ViewmodelRegistrarseFragment(private val repository: Repository):ViewModel() {

    fun registrarNuevoUsuario(usuarioRegistro: UsuarioRegistro) = liveData {


           try {
               val dataNuevoUsuario = JsonObject()

               dataNuevoUsuario.addProperty("name", usuarioRegistro.nombres)
               dataNuevoUsuario.addProperty("phone_number", usuarioRegistro.telefono.toInt())
               dataNuevoUsuario.addProperty("address", usuarioRegistro.direccion)
               dataNuevoUsuario.addProperty("email", usuarioRegistro.correo)
               dataNuevoUsuario.addProperty("password", usuarioRegistro.contrasena)
               dataNuevoUsuario.addProperty("identification_number", usuarioRegistro.numeroDocumento)
               dataNuevoUsuario.addProperty("type_identification_id", usuarioRegistro.tipoDocumento)
               val registroResponse = repository.resgistrarUsuario(dataNuevoUsuario)
               Log.d("regristando", registroResponse.toString())




               emit(registroResponse)

           }catch (e:Exception){
               Log.d("regristando", e.toString())


           }

    }
}