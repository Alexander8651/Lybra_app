package com.amatai.lybra_app.requestmanager.apiresponses

data class ContactosResponse(
    val id: Int? = null,
    val name: String? = null,
    val email: String? = null,
    val number_phone: String? = null,
    val address: String? = null,
    val is_trusted:Int? = null,
    val type_status_id:Int? = null,
    val user_id: Int? = null,
    val created_at:String ? = null,
    val updated_at:String? = null
)