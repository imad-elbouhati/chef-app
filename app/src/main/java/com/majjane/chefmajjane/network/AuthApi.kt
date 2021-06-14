package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.BaseResponse
import com.majjane.chefmajjane.responses.Password
import com.majjane.chefmajjane.responses.SignUp
import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.responses.login.GoogleResponse
import retrofit2.http.*

interface AuthApi {


    @POST("gconnect.php")
    suspend fun postGoogleLogin(
        @Body login: Login
    ): GoogleResponse

    @POST("fconnect.php")
    suspend fun facebookLogin(id_lang: Int, @Query("token") token: String): Int

    @GET("sendOTP.php")
    suspend fun sendOTP(@Query("phone_number") phoneNumber: String): BaseResponse

    @GET("verifyOTP.php")
    suspend fun verifyOTP(
        @Query("code") code: String,
        @Query("phone_number") phoneNumberArg: String
    ): BaseResponse

    @POST("signUp.php")
    suspend fun signUp(@Body signUp: SignUp): BaseResponse


    @POST("login.php")
    suspend fun loginWithEmail(@Body login: Login): BaseResponse

    @POST("updatePassword.php")
    suspend fun updatePassword(@Body password: Password): BaseResponse


}