package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import kotlinx.coroutines.launch

class ViewmodelEditarContactoFragment(private val repository: Repository) : ViewModel() {

    fun actualizarContacto(contactosEntity: ContactosEntity) {
        viewModelScope.launch {
            Log.d("contactoActualizado",contactosEntity.toString())
            repository.actualizarContacto(contactosEntity)
        }
    }
}