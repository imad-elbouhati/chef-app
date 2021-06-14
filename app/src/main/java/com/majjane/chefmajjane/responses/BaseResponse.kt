package com.majjane.chefmajjane.responses

data class BaseResponse(
    val success: Int,
    val message:String,
    val id_customer:Int?,
    val id:Int
)