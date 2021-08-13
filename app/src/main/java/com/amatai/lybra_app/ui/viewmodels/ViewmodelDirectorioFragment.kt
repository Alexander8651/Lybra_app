package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import kotlinx.coroutines.flow.collect
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