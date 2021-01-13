package com.amatai.lybra_app.requestmanager

import androidx.room.Delete
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.amatai.lybra_app.requestmanager.apiresponses.UserResponse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    fun login(@Body dataInicioSesion:JsonObject): Deferred<LogueoResponse>

    @GET("api/directory")
    fun obtenerDirectorio(@Query("token") session:String):Deferred<List<ContactosResponse>>

    @POST("api/directory")
    fun SintronizarContactos(@Body dataCOntacto:JsonObject):Deferred<ContactosResponse>

    @DELETE("api/directory/{id}")
    fun borrarContacto(@Path("id") id:Int):Deferred<ContactosResponse>

    @PUT("api/directory/{id}")
    fun actualizarContacto(@Body dataContacto:JsonObject, @Path("id") id:Int):Deferred<ContactosResponse>

    @POST("api/admin/users")
    fun resgistrarUsuario(@Body dataNuevoUsuario:JsonObject):Deferred<UserResponse>
}