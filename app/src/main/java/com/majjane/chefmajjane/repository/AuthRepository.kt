package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.network.AuthApi


class AuthRepository(val api: AuthApi): BaseRepository() {
    suspend fun postGoogleLogin(email: String?, familyName: String?, givenName: String?,id_lang:Int) = safeApiCall {
        api.postGoogleLogin(email,familyName,givenName,id_lang)
    }

}