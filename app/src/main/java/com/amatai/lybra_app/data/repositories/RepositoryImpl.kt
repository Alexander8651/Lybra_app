package com.amatai.lybra_app.data.repositories

import android.util.Log
import com.amatai.lybra_app.data.DataSources
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
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


    override fun obtenerDirectorioSqlite(): Flow<List<ContactosEntity>> {
        return datasource.obtenerDirectorioSqlite()
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
}