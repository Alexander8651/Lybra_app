package com.amatai.lybra_app.requestmanager.apiresponses

data class ResportesResponse(
    val id:Int?=null,
    val location:String?=null ,
    val longitude:String?=null,
    val latitude:String?=null,
    val user_id:Int? =null,
    val created_at:String?=null,
    val updated_at:String?=null,
    val address:String?=null,
    val city:String?=null,
    val created_rg:String?=null
)