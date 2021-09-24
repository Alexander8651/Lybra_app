package com.amatai.warmi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.ContactosEntity
import com.amatai.warmi.ui.fragments.MainFragment
import kotlinx.coroutines.launch

class ViewmodelAgregarContactoFragment(private val repository: Repository) :ViewModel(){

    fun agregarContactoSqlite(contactosEntity: ContactosEntity){
        contactosEntity.user_id = MainFragment.usuarioLogueado!!.id

        viewModelScope.launch {
            Log.d("WcontactoAgregado", contactosEntity.toString())

            repository.agregarContacto(contactosEntity)
        }
    }

}