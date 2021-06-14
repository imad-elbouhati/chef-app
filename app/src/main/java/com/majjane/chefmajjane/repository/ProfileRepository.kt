package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.network.ProfileApi
import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.responses.ProfileResponse

class ProfileRepository(private val api: ProfileApi):BaseRepository() {


    suspend fun getProfileInfo(customer_id:Int)=safeApiCall {
        api.getProfileInfo(customer_id)
    }

    suspend fun updateProfile(profileResponse: ProfileResponse) = safeApiCall {
        api.updateProfile(profileResponse)
    }
}