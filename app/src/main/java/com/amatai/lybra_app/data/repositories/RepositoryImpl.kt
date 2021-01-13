package com.amatai.lybra_app.data.repositories

import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databasemanager.toVideoEscondido
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.amatai.lybra_app.requestmanager.apiresponses.UserResponse
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
}