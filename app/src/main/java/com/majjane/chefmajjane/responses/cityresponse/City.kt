package com.majjane.chefmajjane.responses.cityresponse

data class City(
    val id: Int,
    val name: String,
    val price: Double
){
    override fun toString(): String {
        return name
    }
}