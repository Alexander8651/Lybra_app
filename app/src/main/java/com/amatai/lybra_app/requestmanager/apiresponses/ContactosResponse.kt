package com.amatai.lybra_app.requestmanager.apiresponses

data class ContactosResponse(
    val id: Int,
    val name: String,
    val email: String,
    val number_phone: String,
    val address: String,
    val is_trusted:Int = 0,
    val type_status_id:Int = 0,
    val user_id: Int,
    val created_at:String =  "",
    val updated_at:String =  ""
)