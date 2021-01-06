package com.amatai.lybra_app.requestmanager.apiresponses

data class UserResponse(
    val id: Int,
    val name: String,
    val identification_number: String ,
    val phone_number: String,
    val status: Int,
    val email: String,
    val image: String,
    val address: String? = null,
    val email_verified_at: String? = null,
    val type_identification_id: Int,
    val type_status_id: Int,
    val created_at: String,
    val updated_at: String
)