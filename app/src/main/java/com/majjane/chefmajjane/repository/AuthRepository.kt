package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.responses.BaseResponse
import com.majjane.chefmajjane.responses.Password
import com.majjane.chefmajjane.responses.SignUp
import com.majjane.chefmajjane.utils.Resource


class AuthRepository(val api: AuthApi) : BaseRepository() {
    suspend fun postGoogleLogin(
        email: String,
        familyName: String,
        givenName: String,
        id_lang: Int
    ) = safeApiCall {
        api.postGoogleLogin(Login(id_lang, givenName, familyName, email, null))
    }

    suspend fun facebookLogin(accessToken: String)= safeApiCall { api.facebookLogin(accessToken) }


    suspend fun sendOTP(phoneNumber: String) = safeApiCall {
        api.sendOTP(phoneNumber)
    }

    suspend fun verifyOTP(code: String, phoneNumberArg: String) = safeApiCall {
        api.verifyOTP(code, phoneNumberArg)
    }

    suspend fun signUp(signUp: SignUp) = safeApiCall {
        api.signUp(signUp)
    }

    suspend fun loginWithEmail(login: Login) = safeApiCall {
        api.loginWithEmail(login)
    }

    suspend fun updatePassword(password: Password) = safeApiCall {
        api.updatePassword(password)
    }

    suspend fun signUpWithPhone(signUp: SignUp) = safeApiCall {
        api.signUpWithPhone(signUp)
    }

}