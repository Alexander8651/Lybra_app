package com.amatai.lybra_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.amatai.lybra_app.data.repositories.Repository
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.VideoEntity
import com.amatai.lybra_app.databasemanager.toContactoEntity
import com.amatai.lybra_app.databasemanager.toContactoEntityList
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class ViewmodelMainFragment(private val repository: Repository) : ViewModel() {

    val obtenerContactosConfianzaSqlite = repository.obtenerContactosConfianzaSqlite()

    val obtenerUsuarioLogueado = liveData {
        try {
            emit(repository.obtenerUsuarioLogueado())
        } catch (e: Exception) {

        }
    }

    fun obtenerContactosSinSincronizar() {
        viewModelScope.launch {
            val contactoSinSincreonizar = repository.obtenerContactosSinSincronizar()

            if (contactoSinSincreonizar.isNotEmpty()) {

                //Log.d("contactosinsincreonizar", contactoSinSincreonizar.toString())

                for (i in contactoSinSincreonizar) {

                    val contacto = i
                    val dataContactoSincronizar = JsonObject()

                    dataContactoSincronizar.addProperty("name", contacto.name)
                    dataContactoSincronizar.addProperty("number_phone", contacto.number_phone)
                    dataContactoSincronizar.addProperty("address", contacto.adress)
                    dataContactoSincronizar.addProperty("email", contacto.email)
                    dataContactoSincronizar.addProperty("user_id", contacto.user_id)

                    val contactoResponse =
                        repository.sincronizarContactosApi(dataContactoSincronizar)
                    val contactoSincronizado = ContactosEntity(
                        contacto.llavePrimariaLocal,
                        1,
                        contactoResponse.id,
                        contactoResponse.name,
                        contactoResponse.email,
                        contactoResponse.number_phone,
                        contactoResponse.address,
                        contactoResponse.is_trusted,
                        contactoResponse.type_status_id,
                        contactoResponse.user_id,
                        contactoResponse.created_at,
                        contactoResponse.updated_at

                    )

                    // Log.d("contactoSincreonizado", contactoResponse.toString())

                    repository.actualizarContacto(contactoSincronizado)

                    //Log.d("contactoSincreonizado", contactoResponse.invoke().toString())
                }
            }
        }
    }

    fun obtenerSessionLogueo() = liveData {
        try {
            emit(repository.obtenerSessionLogueadaSqlite())
        } catch (e: Exception) {
        }
    }

    fun obtenerDirectorio() {
        viewModelScope.launch {

            val contactos = repository.obtenerDirectorioSqlite()
            Log.d("directorioaaorraa", contactos.toString())
            if (contactos.isEmpty()) {
                try {

                    val data = repository.obtenerSessionLogueadaSqlite()

                    val directorio =
                        repository.obtenerDirectorio(data.access_token).toContactoEntityList()
                    Log.d("directorioaaorr", directorio.toString())

                    directorio.map {
                        repository.agregarContacto(it)

                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    fun borrarContactosApi() {
        viewModelScope.launch {
            val directorio = repository.obtenerDirectorioSqlite()

            directorio.map {
                if (it.estadoContacto == 3) {
                    try {
                        val contactosResponseEliminado = repository.borrarContactoAPi(it.id)
                        val contactosEliminado = ContactosEntity(
                            it.llavePrimariaLocal,
                            3,
                            contactosResponseEliminado.id,
                            contactosResponseEliminado.name,
                            contactosResponseEliminado.email,
                            contactosResponseEliminado.number_phone,
                            contactosResponseEliminado.address,
                            contactosResponseEliminado.is_trusted,
                            contactosResponseEliminado.type_status_id,
                            contactosResponseEliminado.user_id,
                            contactosResponseEliminado.created_at,
                            contactosResponseEliminado.updated_at
                        )
                        //Log.d("borrado", contactosResponseEliminado.toString())
                        repository.borrarContactoSqlite(contactosEliminado)
                    } catch (e: Exception) {

                    }

                }
            }
        }
    }

    fun actualizarContactoApi() {
        viewModelScope.launch {
            val directorio = repository.obtenerDirectorioSqlite()
            //Log.d("contactoActualizado", directorio.toString())

            directorio.map {
                if (it.estadoContacto == 2) {
                    try {

                        val dataContactoActualizar = JsonObject()

                        dataContactoActualizar.addProperty("id", it.id)
                        dataContactoActualizar.addProperty("name", it.name)
                        dataContactoActualizar.addProperty("email", it.email)
                        dataContactoActualizar.addProperty("number_phone", it.number_phone)
                        dataContactoActualizar.addProperty("address", it.adress)
                        dataContactoActualizar.addProperty("is_trusted", it.is_trusted)
                        dataContactoActualizar.addProperty("type_status_id", it.type_status_id)
                        dataContactoActualizar.addProperty("user_id", it.user_id)
                        dataContactoActualizar.addProperty("created_at", it.created_at)
                        dataContactoActualizar.addProperty("updated_at", it.updated_at)

                        val contactoResponse =
                            repository.actualizarContactoApi(dataContactoActualizar, it.id)

                        val contactoActualizado = ContactosEntity(
                            it.llavePrimariaLocal,
                            1,
                            contactoResponse.id,
                            contactoResponse.name,
                            contactoResponse.email,
                            contactoResponse.number_phone,
                            contactoResponse.address,
                            contactoResponse.is_trusted,
                            contactoResponse.type_status_id,
                            contactoResponse.user_id,
                            contactoResponse.created_at,
                            contactoResponse.updated_at
                        )

                        Log.d("contactoActualizado", contactoActualizado.toString())

                        repository.actualizarContacto(contactoActualizado)
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    fun agregarVideosSqlite(videoEntity: VideoEntity) {
        viewModelScope.launch {
            repository.agregarVideosSqlite(videoEntity)

        }
    }


}
