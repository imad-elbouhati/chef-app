package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.login.GoogleResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("gconnect.php")
    suspend fun postGoogleLogin(
        @Field("email") email: String?,
        @Field("familyName") familyName: String?,
        @Field("givenName")givenName: String ?,
        @Field("id_lang") id_lang:Int,
    ): GoogleResponse
}