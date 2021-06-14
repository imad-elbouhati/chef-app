package com.majjane.chefmajjane.responses

import com.majjane.chefmajjane.utils.Resource

data class ProfileResponse(
    val id_customer: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone_number: String,
)