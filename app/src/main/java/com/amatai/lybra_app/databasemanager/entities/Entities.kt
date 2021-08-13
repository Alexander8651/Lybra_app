package com.amatai.lybra_app.databasemanager.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "usuariologueado")
data class UsuarioLogueado(

    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "identification_number")
    val identification_number: String,

    @ColumnInfo(name = "phone_number")
    val phone_number: String,

    @ColumnInfo(name = "status")
    val status: Int,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "address")
    val address: String? = null,

    @ColumnInfo(name = "email_verified_at")
    val email_verified_at: String? = null,

    @ColumnInfo(name = "type_identification_id")
    val type_identification_id: Int,

    @ColumnInfo(name = "type_status_id")
    val type_status_id: Int,

    @ColumnInfo(name = "created_at")
    val created_at: String,

    @ColumnInfo(name = "updated_at")
    val updated_at: String
)

@Parcelize
@Entity(tableName = "contactosentity")
data class ContactosEntity(

    @PrimaryKey(autoGenerate = true)
    val llavePrimariaLocal: Int? = 0,

    @ColumnInfo(name = "estadoContacto")
    val estadoContacto: Int,
    /*
    1 -> agregado
    2 -> editado
    3 -> eliminado
     */

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "number_phone")
    val number_phone: String = "",

    @ColumnInfo(name = "adress")
    val adress: String = "",

    @ColumnInfo(name = "is_trusted")
    var is_trusted: Int = 1,

    @ColumnInfo(name = "type_status_id")
    val type_status_id: Int = 0,

    @ColumnInfo(name = "user_id")
    var user_id: Int = 0,

    @ColumnInfo(name = "created_at")
    val created_at: String = "",

    @ColumnInfo(name = "updated_at")
    val updated_at: String = ""
) : Parcelable

@Entity(tableName = "sessionlogueo")
data class SessionLogueo(

    @PrimaryKey(autoGenerate = true)
    val llavePrimariaLocal: Int? = null,

    @ColumnInfo(name = "access_token")
    val access_token: String,

    @ColumnInfo(name = "token_type")
    val token_type: String = "",

    @ColumnInfo(name = "expires_in")
    val expires_in: Int = 0
)

@Parcelize
@Entity(tableName = "videoentity")
data class VideoEntity(

    @PrimaryKey
    val llavePrimariaLocal: Int? = null,

    @ColumnInfo(name = "path")
    val path: String? = null,

    @ColumnInfo(name = "estado")
    val estado: Int? = null
    /*
    1 -> agregado
    2 -> escondido
    3 -> borrado
    */
) : Parcelable

@Parcelize
@Entity(tableName = "audioentity")
data class AudioEntity(

    @PrimaryKey
    val llavePrimariaLocal: Int? = null,

    @ColumnInfo(name = "path")
    val path: String? = null,

    @ColumnInfo(name = "estado")
    val estado: Int? = null
    /*
    1 -> agregado
    2 -> escondido
    3 -> borrado
    */
) : Parcelable

@Entity(tableName = "resportes")
data class ReportesEntity (

    @ColumnInfo(name = "estado")
    val estado: Int? = null,

    @ColumnInfo(name = "id")
    val id:Int? = null,

    @ColumnInfo(name = "location")
    val location:String? = null,

    @ColumnInfo(name = "longitude")
    val longitude:String? = null,

    @ColumnInfo(name = "latitude")
    val latitude:String? = null,

    @ColumnInfo(name = "user_id")
    val user_id:Int? = null,

    @ColumnInfo(name = "created_at")
    val created_at:String? = null,

    @PrimaryKey
    val created_rg:String,

    @ColumnInfo(name = "updated_at")
    val updated_at:String? = null,

    @ColumnInfo(name = "address")
    val address:String? = null,

    @ColumnInfo(name = "city")
    val city:String? = null

)

@Entity(tableName = "configuracion")
data class Configuracion(

    @PrimaryKey
    val llavePrimaria:Int? = null,

    @ColumnInfo(name = "grabarVideoAudio")
    val grabarVideoAudio:Boolean? = false,

    @ColumnInfo(name = "botonPanico")
    val botonPanico:Boolean? = false,

    @ColumnInfo(name = "enviarMensaje")
    val enviarMensaje:Boolean? = false,

    @ColumnInfo(name = "activarNotificacion")
    val activarNotificacion:Boolean? = false
)


