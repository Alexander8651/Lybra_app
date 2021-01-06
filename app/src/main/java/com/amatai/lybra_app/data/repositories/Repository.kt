package com.amatai.lybra_app.data.repositories

import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun login(dataLogeo:JsonObject):LogueoResponse

    suspend fun agregarUsuarioLogueado(usuarioLogueado: UsuarioLogueado)

    suspend fun obtenerUsuarioLogueado():UsuarioLogueado

    suspend fun borrarUsuarioLogueado(usuarioLogueado: UsuarioLogueado)

    suspend fun obtenerDirectorio(dataDirectorio:String):List<ContactosResponse>

    suspend fun agregarDirectorio(agregarDitectorio: List<ContactosEntity>)

    fun obtenerDirectorioSqlite():Flow<List<ContactosEntity>>

    suspend fun guardarSessionLogueadaSqlite(sessionLogueo: SessionLogueo)

    suspend fun obtenerSessionLogueadaSqlite():SessionLogueo

    suspend fun borrarSession(sessionLogueo: SessionLogueo)

    suspend fun borrarDirectorio(directorio:List<ContactosEntity>)
}