package com.amatai.lybra_app.data

data class UsuarioRegistro(
    val nombres:String,
    val correo:String,
    val telefono:String,
    val direccion:String,
    val numeroDocumento:String,
    val contrasena:String,
    val tipoDocumento:Int
)