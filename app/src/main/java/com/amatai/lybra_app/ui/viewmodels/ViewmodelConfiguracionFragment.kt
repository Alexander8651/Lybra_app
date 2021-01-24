package com.amatai.lybra_app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.Configuracion
import kotlinx.coroutines.launch

class ViewmodelConfiguracionFragment (private val repository: Repository):ViewModel(){

    fun guardarConfiguracion(configuracion: Configuracion){
        viewModelScope.launch {
            repository.guardarConfiguraciones(configuracion)
        }
    }

    fun obtenerConfiguracion() = liveData {
        try {
            emit(repository.obtenerConfiguraciones())
        }catch (e:Exception){
        }
    }

    fun actualizarConfiguracion(configuracion: Configuracion){
        viewModelScope.launch {
            repository.actualizarConfiguraciones(configuracion)
        }
    }
}