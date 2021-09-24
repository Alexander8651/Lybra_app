package com.amatai.warmi.databasemanager

import com.amatai.warmi.databasemanager.entities.*
import com.amatai.warmi.requestmanager.apiresponses.ContactosResponse
import com.amatai.warmi.requestmanager.apiresponses.ResportesResponse
import com.amatai.warmi.requestmanager.apiresponses.UserResponse

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
    1,
    id!!,
    name!!,
    email!!,
    number_phone!!,
    address!!,
    is_trusted!!,
    type_status_id!!,
    user_id!!,
    created_at!!,
    updated_at!!
)

fun List<ResportesResponse>.toReporteEntity() = map(ResportesResponse::toReporteEntity)
fun ResportesResponse.toReporteEntity() = ReportesEntity(
    1,
    id,
    location,
    longitude,
    latitude,
    user_id,
    created_at,
    created_rg!!,
    updated_at,
    address,
    city
)

fun VideoEntity.toVideoEscondido() = VideoEntity(
    llavePrimariaLocal,
    path,
    2
)

fun VideoEntity.toVideoVisible() = VideoEntity(
    llavePrimariaLocal,
    path,
    1
)

fun VideoEntity.toVideoBorrado() = VideoEntity(
    llavePrimariaLocal,
    path,
    3
)

fun AudioEntity.toAudioEscondido() = AudioEntity(
    llavePrimariaLocal,
    path,
    2
)

fun AudioEntity.toAudioVisible() = AudioEntity(
    llavePrimariaLocal,
    path,
    1
)