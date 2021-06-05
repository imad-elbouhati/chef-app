package com.majjane.chefmajjane.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi {
    @GET("getAdresseLivraison.php")
    suspend fun getAddress(@Query("id_customer")  idCustomer:Int)
}