package com.amatai.lybra_app.requestmanager

import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.LogueoResponse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    fun login(@Body dataInicioSesion:JsonObject): Deferred<LogueoResponse>

    @GET("api/directory")
    fun obtenerDirectorio(@Query("token") session:String):Deferred<List<ContactosResponse>>
}