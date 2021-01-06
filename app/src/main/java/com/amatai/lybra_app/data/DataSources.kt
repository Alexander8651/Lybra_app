package com.amatai.lybra_app.data

import android.util.Log
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.requestmanager.RetrofitService
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

class DataSources (private val appDatabase: AppDatabase){

    suspend fun login(dataLogueo:JsonObject):LogueoResponse{
        return RetrofitService.retrofitService.login(dataLogueo).await()
    }

    suspend fun agrebarUsuarioLogueado(usuarioLogueado: UsuarioLogueado){
     appDatabase.appDao().agregarUsuarioLogueado(usuarioLogueado)
    }

    suspend fun agregarSessionSqlite(sessionLogueo: SessionLogueo){
        appDatabase.appDao().agregarSessionLogueada(sessionLogueo)
    }

    suspend fun obteneterSessioSqlite():SessionLogueo{
        return appDatabase.appDao().obtenerSession()
    }

    fun borrarSessionLogueada(sessionLogueo: SessionLogueo){
        appDatabase.appDao().borrarSessionLogueada(sessionLogueo)
    }

    suspend fun obtenerUsuarioLogueado():UsuarioLogueado{
        return appDatabase.appDao().obtenerUsuarioLogueado()
    }

    suspend fun borrarUsuarioLoueado(usuarioLogueado: UsuarioLogueado){
        appDatabase.appDao().borrarUsuarioLogueado(usuarioLogueado)
    }

    suspend fun obtenerDirectorio(dataDirectorio:String):List<ContactosResponse>{
        return  RetrofitService.retrofitService.obtenerDirectorio(dataDirectorio).await()
    }

    suspend fun agregarDirectorioSqlite(directorio:List<ContactosEntity>){
        appDatabase.appDao().agredarDirectorio(directorio)
    }

    fun obtenerDirectorioSqlite():Flow<List<ContactosEntity>>{
        return appDatabase.appDao().obtenerDirectorio()
    }

    suspend fun borrarDirectorioSqlite(directorio:List<ContactosEntity>){
        appDatabase.appDao().borrarDirectorio(directorio)
    }

}