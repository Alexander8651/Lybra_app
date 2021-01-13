package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.ui.activities.MainActivity
import com.amatai.lybra_app.ui.fragments.MainFragment
import kotlinx.coroutines.launch

class ViewmodelAgregarContactoFragment(private val repository: Repository) :ViewModel(){

    fun agregarContactoSqlite(contactosEntity: ContactosEntity){
        contactosEntity.user_id = MainFragment.usuarioLogueado!!.id

        viewModelScope.launch {
            repository.agregarContacto(contactosEntity)
        }
    }

}