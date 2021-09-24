package com.amatai.warmi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amatai.warmi.data.repositories.Repository
import com.amatai.warmi.databasemanager.entities.ContactosEntity
import com.amatai.warmi.databasemanager.entities.SessionLogueo
import com.amatai.warmi.databasemanager.entities.UsuarioLogueado
import kotlinx.coroutines.launch

class ViewmodelSalirFragment(private val reposotiry: Repository) : ViewModel() {

    val obtenerUsuarioLogueado = liveData {
        try {

            emit(reposotiry.obtenerUsuarioLogueado())

        } catch (e: Exception) {

        }
    }

    val obtenerSessionLogueada = liveData {
        try {
            emit(reposotiry.obtenerSessionLogueadaSqlite())
        } catch (e: Exception) {

        }
    }

    val obtenerDirectorio = liveData {
        try {
            emit(reposotiry.obtenerDirectorioSqlite())
        }catch (e:Exception){

        }
    }

    fun borrarReportes (){
        viewModelScope.launch {

            val reportes = reposotiry.obtenerReportesSqlite()

            reposotiry.borrarReportes(reportes)
        }
    }

    fun borrarUsuarioLogueado(usuarioLogueado: UsuarioLogueado) {
        viewModelScope.launch {
            reposotiry.borrarUsuarioLogueado(usuarioLogueado)
        }
    }

    fun borrarSessionLogueada(sessionLogueo: SessionLogueo) {
        viewModelScope.launch {
            reposotiry.borrarSession(sessionLogueo)
        }
    }

    fun borrarDirectorio(directorio: List<ContactosEntity>) {
        viewModelScope.launch {
            reposotiry.borrarDirectorio(directorio)
        }
    }

}