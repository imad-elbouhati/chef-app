package com.majjane.chefmajjane.responses

data class SignUp(
    val email: String,
    val first_name: String,
    val id_lang: Int,
    val last_name: String,
    val password: String,
    val phone_number: String
)