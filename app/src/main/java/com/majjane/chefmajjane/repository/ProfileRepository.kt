package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.network.ProfileApi
import com.majjane.chefmajjane.repository.base.BaseRepository

class ProfileRepository(private val api: ProfileApi):BaseRepository() {


    suspend fun getProfileInfo(customer_id:Int)=safeApiCall {
        api.getProfileInfo(customer_id)
    }
}