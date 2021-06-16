package com.majjane.chefmajjane.responses.model

data class CommandeModel(
    val address: String,
    val id_city: Int,
    val id_customer: Int,
    val id_method_payment: Int,
    val phone_number: String,
    val postcode: String,
    val products: List<Product>,
    val promo: String
)