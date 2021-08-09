package com.amatai.lybra_app.data

import android.util.Log
import com.amatai.lybra_app.databasemanager.*
import com.amatai.lybra_app.databasemanager.entities.*
import com.amatai.lybra_app.requestmanager.RetrofitService
import com.amatai.lybra_app.requestmanager.apiresponses.*
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat

class DataSources (private val appDatabase: AppDatabase){

    private val FILENAME_FORMAT = "yyyy-MM-dd"
    private val FILENAME_FORMATAUDIO = "yyyyMMdd"

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
        var video: VideoEntity? = null

        val fecha = SimpleDateFormat(FILENAME_FORMAT).format(System.currentTimeMillis())
        Log.d("substring", fecha)
        Log.d("substring", nombre.toString())


         if (nombre == fecha){
             video = videoEntity.toVideoEscondido()
        }else{
             video = videoEntity.toVideoVisible()

           // val path:String = videoEntity.path
           // val video:File = File(path)
            //video.delete()
            //videoEntity.toVideoBorrado()

        }
        appDatabase.appDao().actualizarEstadoVideo(video!!)
    }

    suspend fun listarAlertasApi(token:String):List<ResportesResponse>{
        return RetrofitService.retrofitService.listarAlertas(token).await()
    }

    suspend fun agredarReportesSqlite(reportes:List<ReportesEntity>){
        appDatabase.appDao().agredarReportes(reportes)
    }

    suspend fun obtenerReportesSqlite():List<ReportesEntity>{
        return  appDatabase.appDao().obtenerReportes()
    }

    suspend fun agredarReporte(reportes:ReportesEntity){
        appDatabase.appDao().agredarReporte(reportes)
    }

    suspend fun obtenerReportesSinSincronizarSqlite():List<ReportesEntity>{
        return appDatabase.appDao().obtenerReportesSinSincronizar()
    }

    suspend fun actualizarReporteSqlite(reportes:ReportesEntity){
        appDatabase.appDao().actualizarReporte(reportes)
    }

    suspend fun borrarReportes(reportes:List<ReportesEntity>){
        appDatabase.appDao().borrarReportes(reportes)
    }

    suspend fun registrarReporteApi(dataReporte:JsonObject): ResportesResponse {
        return RetrofitService.retrofitService.registrarReporte(dataReporte).await()
    }


    suspend fun guardarConfiguraciones(configuracion: Configuracion){

        appDatabase.appDao().guardarConfiguraciones(configuracion)

    }


    suspend fun actualizarConfiguraciones(configuracion: Configuracion){
        appDatabase.appDao().actualizarConfiguraciones(configuracion)
    }


    suspend fun obtenerConfiguraciones():Configuracion{
        return appDatabase.appDao().obtenerConfiguraciones()
    }

    suspend fun listarNotificaciones(token:String):List<Notification>{
        return RetrofitService.retrofitService.listarNotificaciones(token).await()
    }


    suspend fun guardarAudio(audio: AudioEntity){
        appDatabase.appDao().guardarAudio(audio)
    }

    suspend fun obtenerAudios():List<AudioEntity>{
        return appDatabase.appDao().obtenerAudios()
    }

    suspend fun actualizarEstadoAudioSqlite(audioEntity: AudioEntity) {
        val nombre = audioEntity.path!!.subSequence(61..68 )

        //Log.d("substring", videoEntity.path)
        var audio: AudioEntity? = null

        val fecha = SimpleDateFormat(FILENAME_FORMATAUDIO).format(System.currentTimeMillis())
        Log.d("substring", fecha)
        Log.d("substring", nombre.toString())

        if (nombre == fecha){
            audio = audioEntity.toAudioEscondido()
        }else{

            audio = audioEntity.toAudioVisible()
            Log.d("substringaca", nombre.toString())
        }
        appDatabase.appDao().actualizarAudio(audio!!)
    }
}