package com.amatai.lybra_app.databasemanager

import androidx.room.*
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarUsuarioLogueado(usuarioLogureado:UsuarioLogueado)

    @Query("SELECT * FROM usuariologueado")
    fun obtenerUsuarioLogueado():UsuarioLogueado

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarSessionLogueada(session: SessionLogueo)

    @Query("SELECT * FROM sessionlogueo")
    suspend fun obtenerSession():SessionLogueo

    @Delete
    fun borrarSessionLogueada(session: SessionLogueo)

    @Delete
    suspend fun borrarUsuarioLogueado(usuarioLogureado: UsuarioLogueado)

    @Insert
    suspend fun agredarDirectorio(directorio:List<ContactosEntity>)

    @Query("SELECT * FROM   CONTACTOSENTITY")
    fun obtenerDirectorio():Flow<List<ContactosEntity>>

    @Delete
    suspend fun borrarDirectorio(directorio:List<ContactosEntity>)



}