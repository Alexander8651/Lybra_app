package com.amatai.warmi.data

data class UsuarioRegistro(
    val nombres:String,
    val correo:String,
    val telefono:String,
    val direccion:String,
    val numeroDocumento:String,
    val contrasena:String,
    val tipoDocumento:Int
)