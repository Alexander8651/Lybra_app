package com.amatai.warmi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.ContactosEntity
import kotlinx.coroutines.launch

class ViewmodelEditarContactoFragment(private val repository: Repository) : ViewModel() {

    fun actualizarContacto(contactosEntity: ContactosEntity) {
        viewModelScope.launch {
            Log.d("contactoActualizado",contactosEntity.toString())
            repository.actualizarContacto(contactosEntity)
        }
    }
}