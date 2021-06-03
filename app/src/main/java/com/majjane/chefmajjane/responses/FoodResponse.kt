package com.majjane.chefmajjane.responses

data class FoodResponse(
    val articles: List<Article>,
    val total_products: Int
)