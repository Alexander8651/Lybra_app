package com.amatai.warmi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.Configuracion
import com.amatai.warmi.databasemanager.entities.SessionLogueo
import com.amatai.warmi.databasemanager.toUserLogueado
import com.amatai.warmi.requestmanager.apiresponses.UserResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.lang.Exception


class ViewmodelLogin(private val repository: Repository) :ViewModel(){

    private val _logueo = MutableLiveData<Boolean>()

    val logueo:LiveData<Boolean>
            get() = _logueo


    fun loguin(dataLogin:JsonObject) = liveData {
        try {
            emit(repository.login(dataLogin))
        }catch (e:Exception){
            //aca puedo manejar la excepcion pero voy a preguntar si se puede modificar la respuesta
            Log.d("errorrr", e.toString())

            _logueo.postValue(true)


        }
    }

    fun agregarUsuarioLogueado(usuarioLogueado: UserResponse) {
       viewModelScope.launch {
           val usuarioLogueadoTransformado = usuarioLogueado.toUserLogueado()
           repository.agregarUsuarioLogueado(usuarioLogueadoTransformado)
       }
    }

    fun obtenerUsuarioLogueado() = liveData {
        try {
            emit(repository.obtenerUsuarioLogueado())
        }catch (e:Exception){

        }
    }

    fun guardarSession(sessionLogueo: SessionLogueo){
        viewModelScope.launch {
            repository.guardarSessionLogueadaSqlite(sessionLogueo)
        }
    }

    val obtenerConfiguracion = liveData {
        try {
            emit(repository.obtenerConfiguraciones())
        } catch (e: Exception) {

        }
    }

    fun guardarConfiguracion(configuracion: Configuracion){
        viewModelScope.launch {
            repository.guardarConfiguraciones(configuracion)
        }
    }

}