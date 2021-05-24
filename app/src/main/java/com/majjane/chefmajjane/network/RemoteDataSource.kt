package com.majjane.chefmajjane.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    companion object{
        const val BASE_URL = "https://bo.homeopp.ma/Boutique/"
    }
    fun <Api> buildApi(
        api:Class<Api>
    ):Api{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
}