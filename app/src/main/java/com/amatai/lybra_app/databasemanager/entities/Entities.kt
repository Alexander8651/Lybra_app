package com.amatai.lybra_app.databasemanager.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

@Entity(tableName = "contactosentity")
data class ContactosEntity(

    @PrimaryKey(autoGenerate = true)
    val idLlavePrimaria: Long? = null,

    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "numberPhone")
    val number_Phone: String,

    @ColumnInfo(name = "adress")
    val adress: String,

    @ColumnInfo(name = "is_trusted")
    val is_trusted:Int = 0,

    @ColumnInfo(name = "type_status_id")
    val type_status_id:Int = 0,

    @ColumnInfo(name = "user_id")
    val user_id: Int,

    @ColumnInfo(name = "created_at")
    val created_at:String =  "2",

    @ColumnInfo(name = "updated_at")
    val updated_at:String =  ""
)

@Entity(tableName = "sessionlogueo")
data class SessionLogueo(

    @PrimaryKey(autoGenerate = true)
    val llavePrimariaLocal:Int? = null,

    @ColumnInfo(name = "access_token")
    val access_token:String,

    @ColumnInfo(name = "token_type")
    val token_type: String = "",

    @ColumnInfo( name = "expires_in")
    val expires_in: Int = 0
)

