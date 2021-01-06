package com.amatai.lybra_app.requestmanager.apiresponses

data class LogueoResponse (
    val error:String = "",
    val access_token:String = "",
    val token_type:String,
    val expires_in:Int,
    val user:UserResponse
)