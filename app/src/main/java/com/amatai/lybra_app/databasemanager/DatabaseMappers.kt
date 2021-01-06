package com.amatai.lybra_app.databasemanager

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.amatai.lybra_app.databasemanager.entities.ContactosEntity
import com.amatai.lybra_app.databasemanager.entities.UsuarioLogueado
import com.amatai.lybra_app.requestmanager.apiresponses.ContactosResponse
import com.amatai.lybra_app.requestmanager.apiresponses.UserResponse

fun UserResponse.toUserLogueado() = UsuarioLogueado(
    id,
    name,
    identification_number,
    phone_number,
    status,
    email,
    image,
    address,
    email_verified_at,
    type_identification_id,
    type_status_id,
    created_at,
    updated_at
)

fun List<ContactosResponse>.toContactoEntityList() = map(ContactosResponse::toContactoEntity)
fun ContactosResponse.toContactoEntity() = ContactosEntity(
    null,
    id,
    name,
    email,
    number_phone,
    address,
    is_trusted,
    type_status_id,
    user_id,
    created_at,
    updated_at
)