package com.amatai.lybra_app.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.VideoCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ViewmodelMainFragment(private val repository: Repository) : ViewModel() {

    val obtenerDirectorioSqlite = repository.obtenerDirectorioSqlite()

    val obtenerUsuarioLogueado = liveData {
        try {

            emit(repository.obtenerUsuarioLogueado())
        }catch (e:Exception){

        }
    }

    fun obtenerSessionLogueo() = liveData {
        try {
            emit(repository.obtenerSessionLogueadaSqlite())
        } catch (e: Exception) {
        }
    }

    fun obtenerDirectorio() {
        viewModelScope.launch {
           try {

               val data = repository.obtenerSessionLogueadaSqlite()

               val directorio = repository.obtenerDirectorio(data.access_token).map {
                   ContactosEntity(
                       null,
                       it.id,
                       it.name,
                       it.email,
                       it.number_phone,
                       it.address,
                       it.is_trusted,
                       it.type_status_id,
                       it.user_id,
                       it.created_at,
                       it.updated_at
                   )
               }
               //Log.d("directorioaao", directorio.toString())

               obtenerDirectorioSqlite.collect {
                   if (it.isNullOrEmpty()){
                       repository.agregarDirectorio(directorio)
                   }
                   //Log.d("directorioSqlite", it.toString())
               }

           }catch (e:Exception){

           }
        }
    }


}