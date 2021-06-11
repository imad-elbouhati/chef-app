package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.network.AuthApi


class AuthRepository(val api: AuthApi) : BaseRepository() {
    suspend fun postGoogleLogin(
        email: String,
        familyName: String,
        givenName: String,
        id_lang: Int
    ) = safeApiCall {
        api.postGoogleLogin(Login(id_lang, givenName, familyName, email))
    }

    suspend fun facebookLogin(id_lang: Int, accessToken: String) = safeApiCall {
        api.facebookLogin(id_lang, accessToken)
    }

    suspend fun sendOTP(phoneNumber: String) = safeApiCall {
        api.sendOTP(phoneNumber)
    }

}