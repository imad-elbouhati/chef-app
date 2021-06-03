package com.majjane.chefmajjane.responses

data class Article(
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val prixFinal: String,
    val prixTTC: String,
    val qnt: Int,
    var selectedQuantity: Int = 0,
    val reduction: String
)