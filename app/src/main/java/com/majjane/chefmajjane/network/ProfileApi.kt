package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.BaseResponse
import com.majjane.chefmajjane.responses.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileApi {

    @GET("getInfoProfil.php")
    suspend fun getProfileInfo(@Query("id_customer") id_customer:Int):ProfileResponse

    @POST("updateProfil.php")
    suspend fun updateProfile(@Body profileResponse: ProfileResponse):BaseResponse
}