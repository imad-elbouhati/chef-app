package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.ReclamationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReclamationApi {


    @Multipart
    @POST("paymentjustify.php")
    fun uploadReclamation(
        @Part image: MultipartBody.Part,
        @Part("comment") comment: RequestBody,
        @Part("numCom") numCom: RequestBody,
        @Part("object") `object`: RequestBody,
        @Part("id_customer") id_customer: RequestBody,
        @Part("isJustification") isJustification: RequestBody
    ): Call<ReclamationResponse>

}