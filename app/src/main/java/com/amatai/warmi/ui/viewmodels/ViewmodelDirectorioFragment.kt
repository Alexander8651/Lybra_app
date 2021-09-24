package com.amatai.warmi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.ContactosEntity
import java.lang.Exception

class ViewmodelDirectorioFragment(private val repository: Repository) : ViewModel() {

    fun obtenerDirecotioSqlite() = liveData {
        try {

            val directotio:MutableList<ContactosEntity> = mutableListOf()

            val directorio = repository.obtenerDirectorioSqlite()

                directorio.map {
                    if (it.estadoContacto != 3){
                        directotio.add(it)

                    }

                emit(directotio)
            }

        }catch (e:Exception){

        }
    }
}