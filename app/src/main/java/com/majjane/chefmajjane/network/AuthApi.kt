package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.responses.login.GoogleResponse
import retrofit2.http.*

interface AuthApi {

//    @FormUrlEncoded
//    @POST("gconnect.php")
//    suspend fun postGoogleLogin(
//        @Field("id_lang") id_lang: Int,
//        @Field("nom") familyName: String?,
//        @Field("prenom") givenName: String?,
//        @Field("email") email: String?
//    ): GoogleResponse

    @POST("gconnect.php")
    suspend fun postGoogleLogin(
       @Body login: Login
    ): GoogleResponse

    @POST("fconnect.php")
    fun facebookLogin(id_lang: Int, @Query("token") token: String):Int

}