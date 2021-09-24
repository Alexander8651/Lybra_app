package com.amatai.warmi.data.repositories

import com.amatai.warmi.data.DataSources
import com.amatai.warmi.databasemanager.entities.*
import com.amatai.warmi.requestmanager.apiresponses.*
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val datasource: DataSources):Repository {

    override suspend fun login(dataLogeo: JsonObject):LogueoResponse {

        return  datasource.login(dataLogeo)
    }

    override suspend fun agregarUsuarioLogueado(usuarioLogueado: UsuarioLogueado) {
        datasource.agrebarUsuarioLogueado(usuarioLogueado)
    }

    override suspend fun obtenerUsuarioLogueado(): UsuarioLogueado {
       return datasource.obtenerUsuarioLogueado()
    }

    override suspend fun borrarUsuarioLogueado(usuarioLogueado: UsuarioLogueado) {
        datasource.borrarUsuarioLoueado(usuarioLogueado)
    }

    override suspend fun obtenerDirectorio(dataSessionLogueo: String): List<ContactosResponse> {
        return datasource.obtenerDirectorio(dataSessionLogueo)
    }

    override suspend fun agregarDirectorio(agregarDitectorio: List<ContactosEntity>) {
       datasource.agregarDirectorioSqlite(agregarDitectorio)
    }


    override suspend fun obtenerDirectorioSqlite(): List<ContactosEntity> {
        return datasource.obtenerDirectorioSqlite()
    }

    override fun obtenerContactosConfianzaSqlite(): Flow<List<ContactosEntity>> {
        return datasource.obtenerContactosConfianzaSqlite()
    }

    override suspend fun guardarSessionLogueadaSqlite(sessionLogueo: SessionLogueo) {
      datasource.agregarSessionSqlite(sessionLogueo)
    }

    override suspend fun obtenerSessionLogueadaSqlite(): SessionLogueo {
        return datasource.obteneterSessioSqlite()
    }

    override suspend fun borrarSession(sessionLogueo: SessionLogueo) {
        datasource.borrarSessionLogueada(sessionLogueo)
    }

    override suspend fun borrarDirectorio(directorio: List<ContactosEntity>) {
        datasource.borrarDirectorioSqlite(directorio)
    }

    override suspend fun agregarContacto(contactosEntity: ContactosEntity) {
        datasource.agregarContacto(contactosEntity)
    }

    override suspend fun obtenerContactosSinSincronizar(): List<ContactosEntity> {
        return datasource.obtenerContactosSinSincronizar()
    }

    override suspend fun sincronizarContactosApi(contactoData:JsonObject): ContactosResponse {
        return datasource.sincronizarContactosApi(contactoData)
    }

    override suspend fun actualizarContacto(contactosEntity: ContactosEntity) {
        datasource.actualizarContacto(contactosEntity)
    }

    override suspend fun borrarContactoAPi(id: Int): ContactosResponse {
        return datasource.borrarContactoAPi(id)
    }

    override suspend fun borrarContactoSqlite(contactosEntity: ContactosEntity) {
        datasource.borrarContactoSqlite(contactosEntity)
    }

    override suspend fun actualizarContactoApi(dataContacto: JsonObject, id:Int):ContactosResponse {
        return datasource.actualizarContactoApi(dataContacto, id)
    }

    override suspend fun resgistrarUsuario(dataNuevoUsuario: JsonObject): UserResponse {
        return datasource.resgistrarUsuario(dataNuevoUsuario)
    }

    override suspend fun agregarVideosSqlite(videoEntity: VideoEntity) {
        datasource.agregarVideosSqlite(videoEntity)
    }

    override suspend fun obtenerVideosSqlite(): List<VideoEntity> {
        return datasource.obtenerVideosSqlite()
    }

    override suspend fun actualizarEstadoVideoSqlite(videoEntity: VideoEntity) {
        datasource.actualizarEstadoVideoSqlite(videoEntity)
    }

    override suspend fun listarAlertasApi(token: String): List<ResportesResponse> {
        return datasource.listarAlertasApi(token)
    }

    override suspend fun agredarReportesSqlite(reportes: List<ReportesEntity>) {
        datasource.agredarReportesSqlite(reportes)
    }

    override suspend fun obtenerReportesSqlite(): List<ReportesEntity> {
        return datasource.obtenerReportesSqlite()
    }

    override suspend fun agredarReporte(reporte: ReportesEntity) {
        datasource.agredarReporte(reporte)
    }

    override suspend fun obtenerReportesSinSincronizarSqlite(): List<ReportesEntity> {
        return datasource.obtenerReportesSinSincronizarSqlite()
    }

    override suspend fun actualizarReporteSqlite(reportes: ReportesEntity) {
        datasource.actualizarReporteSqlite(reportes)
    }

    override suspend fun borrarReportes(reportes: List<ReportesEntity>) {
        datasource.borrarReportes(reportes)
    }

    override suspend fun registrarReporteApi(dataReporte: JsonObject): ResportesResponse {
        return datasource.registrarReporteApi(dataReporte)
    }

    override suspend fun guardarConfiguraciones(configuracion: Configuracion) {
        datasource.guardarConfiguraciones(configuracion)
    }

    override suspend fun actualizarConfiguraciones(configuracion: Configuracion) {
        datasource.actualizarConfiguraciones(configuracion)
    }

    override suspend fun obtenerConfiguraciones(): Configuracion {
        return datasource.obtenerConfiguraciones()
    }

    override suspend fun listarNotificaciones(token: String): List<Notification> {
        return datasource.listarNotificaciones(token)
    }

    override suspend fun guardarAudio(audio: AudioEntity) {
        datasource.guardarAudio(audio)
    }

    override suspend fun obtenerAudios(): List<AudioEntity> {
        return datasource.obtenerAudios()
    }

    override suspend fun actualizarEstadoAudioSqlite(audio: AudioEntity) {
        datasource.actualizarEstadoAudioSqlite(audio)
    }
}