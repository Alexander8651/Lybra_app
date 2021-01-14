package com.amatai.lybra_app.data

import android.util.Log
import androidx.room.Query
import androidx.room.Update
import com.amatai.lybra_app.databasemanager.AppDatabase
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databasemanager.toVideoBorrado
import com.amatai.lybra_app.databasemanager.toVideoEscondido
import com.amatai.lybra_app.requestmanager.RetrofitService
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.amatai.lybra_app.requestmanager.apiresponses.UserResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DataSources (private val appDatabase: AppDatabase){

    private val FILENAME_FORMAT = "yyyy-MM-dd"

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

    suspend fun obtenerDirectorioSqlite():List<ContactosEntity>{
        return appDatabase.appDao().obtenerDirectorio()
    }

    fun obtenerContactosConfianzaSqlite():Flow<List<ContactosEntity>>{
        return appDatabase.appDao().obtenerContactosConfianza()
    }

    suspend fun borrarDirectorioSqlite(directorio:List<ContactosEntity>){
        appDatabase.appDao().borrarDirectorio(directorio)
    }

    suspend fun agregarContacto(contactosEntity: ContactosEntity){
        appDatabase.appDao().agregarContacto(contactosEntity)
    }

    suspend fun obtenerContactosSinSincronizar():List<ContactosEntity>{
        return appDatabase.appDao().obtenerContactosSinSincronizar()
    }

    suspend fun sincronizarContactosApi(dataContacto:JsonObject):ContactosResponse{
        return RetrofitService.retrofitService.SintronizarContactos(dataContacto).await()
    }

    suspend fun actualizarContacto(contactosEntity: ContactosEntity){
        appDatabase.appDao().actualizarContacto(contactosEntity)
    }

    suspend fun borrarContactoAPi(id:Int):ContactosResponse{
        return  RetrofitService.retrofitService.borrarContacto(id).await()
    }

    suspend fun borrarContactoSqlite(contactosEntity: ContactosEntity){
        appDatabase.appDao().borrarContacto(contactosEntity)
    }

    suspend fun actualizarContactoApi(dataContacto:JsonObject, id:Int):ContactosResponse{
        return RetrofitService.retrofitService.actualizarContacto(dataContacto, id).await()
    }

    suspend fun resgistrarUsuario(dataNuevoUsuario:JsonObject):UserResponse{
        return RetrofitService.retrofitService.resgistrarUsuario(dataNuevoUsuario).await()
    }

    suspend fun agregarVideosSqlite(videoEntity: VideoEntity){
        appDatabase.appDao().agregarVideo(videoEntity)
    }


    suspend fun obtenerVideosSqlite():List<VideoEntity>{
        return appDatabase.appDao().obtenerVideos()
    }

    suspend fun actualizarEstadoVideoSqlite(videoEntity: VideoEntity){
        val nombre = videoEntity.path!!.subSequence(55..64 )
        //Log.d("substring", videoEntity.path)
        val video: VideoEntity

        val fecha = SimpleDateFormat(FILENAME_FORMAT).format(System.currentTimeMillis())
        Log.d("substring", fecha)
        Log.d("substring", nombre.toString())


        video = if (nombre == fecha){
            videoEntity.toVideoEscondido()
        }else{

            val path:String = videoEntity.path
            val video:File = File(path)
            //video.delete()
            videoEntity.toVideoBorrado()

        }
        appDatabase.appDao().actualizarEstadoVideo(video)
    }

}