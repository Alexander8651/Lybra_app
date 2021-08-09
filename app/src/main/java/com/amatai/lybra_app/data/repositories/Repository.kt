package com.amatai.lybra_app.data.repositories

import android.provider.MediaStore
import com.amatai.lybra_app.databasemanager.entities.*
import com.amatai.lybra_app.requestmanager.apiresponses.*
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun login(dataLogeo:JsonObject):LogueoResponse

    suspend fun agregarUsuarioLogueado(usuarioLogueado: UsuarioLogueado)

    suspend fun obtenerUsuarioLogueado():UsuarioLogueado

    suspend fun borrarUsuarioLogueado(usuarioLogueado: UsuarioLogueado)

    suspend fun obtenerDirectorio(dataDirectorio:String):List<ContactosResponse>

    suspend fun agregarDirectorio(agregarDitectorio: List<ContactosEntity>)

    suspend fun obtenerDirectorioSqlite():List<ContactosEntity>

    fun obtenerContactosConfianzaSqlite():Flow<List<ContactosEntity>>

    suspend fun guardarSessionLogueadaSqlite(sessionLogueo: SessionLogueo)

    suspend fun obtenerSessionLogueadaSqlite():SessionLogueo

    suspend fun borrarSession(sessionLogueo: SessionLogueo)

    suspend fun borrarDirectorio(directorio:List<ContactosEntity>)

    suspend fun agregarContacto(contactosEntity: ContactosEntity)

    suspend fun obtenerContactosSinSincronizar():List<ContactosEntity>

    suspend fun sincronizarContactosApi(dataContacto:JsonObject):ContactosResponse

    suspend fun actualizarContacto(contactosEntity: ContactosEntity)

    suspend fun borrarContactoAPi(id:Int):ContactosResponse

    suspend fun borrarContactoSqlite(contactosEntity: ContactosEntity)

    suspend fun actualizarContactoApi(dataContacto:JsonObject, id:Int):ContactosResponse

    suspend fun resgistrarUsuario(dataNuevoUsuario:JsonObject): UserResponse

    suspend fun agregarVideosSqlite(videoEntity: VideoEntity)

    suspend fun obtenerVideosSqlite():List<VideoEntity>

    suspend fun actualizarEstadoVideoSqlite(videoEntity: VideoEntity)

    suspend fun listarAlertasApi(token:String):List<ResportesResponse>

    suspend fun agredarReportesSqlite(reportes:List<ReportesEntity>)

    suspend fun obtenerReportesSqlite():List<ReportesEntity>

    suspend fun agredarReporte(reporte:ReportesEntity)

    suspend fun obtenerReportesSinSincronizarSqlite():List<ReportesEntity>

    suspend fun actualizarReporteSqlite(reportes:ReportesEntity)

    suspend fun borrarReportes(reportes:List<ReportesEntity>)

    suspend fun registrarReporteApi(dataReporte:JsonObject): ResportesResponse

    suspend fun guardarConfiguraciones(configuracion: Configuracion)


    suspend fun actualizarConfiguraciones(configuracion: Configuracion)


    suspend fun obtenerConfiguraciones():Configuracion

    suspend fun listarNotificaciones(token:String):List<Notification>

    suspend fun guardarAudio(audio: AudioEntity)

    suspend fun obtenerAudios():List<AudioEntity>

    suspend fun actualizarEstadoAudioSqlite(audio: AudioEntity)

}