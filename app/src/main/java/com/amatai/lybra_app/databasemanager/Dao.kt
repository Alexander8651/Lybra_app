package com.amatai.lybra_app.databasemanager

import androidx.room.*
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.SessionLogueo
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
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

    @Query("SELECT * FROM CONTACTOSENTITY ")
    suspend fun obtenerDirectorio():List<ContactosEntity>

    @Query("SELECT * FROM CONTACTOSENTITY WHERE is_trusted == 1")
    fun obtenerContactosConfianza():Flow<List<ContactosEntity>>

    @Query("SELECT * FROM CONTACTOSENTITY WHERE id == 0")
    suspend fun obtenerContactosSinSincronizar():List<ContactosEntity>

    @Update
    suspend fun actualizarContacto(contactosEntity: ContactosEntity)

    @Delete
    suspend fun borrarDirectorio(directorio:List<ContactosEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun agregarContacto(contactosEntity: ContactosEntity)

    @Delete
    suspend fun borrarContacto(contactosEntity: ContactosEntity)

    @Insert
    suspend fun agregarVideo(videoEntity: VideoEntity)

    @Query("SELECT * FROM videoentity")
    suspend fun obtenerVideos():List<VideoEntity>

    @Update
    suspend fun actualizarEstadoVideo(videoEntity: VideoEntity)

}