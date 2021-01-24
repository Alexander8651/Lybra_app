package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.databasemanager.toUserLogueado
import com.amatai.lybra_app.requestmanager.apiresponses.UserResponse
import com.amatai.lybra_app.ui.fragments.MainFragment
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



}