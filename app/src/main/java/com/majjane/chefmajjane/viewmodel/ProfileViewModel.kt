package com.majjane.chefmajjane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majjane.chefmajjane.repository.ProfileRepository
import com.majjane.chefmajjane.responses.ProfileResponse
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository): ViewModel() {

    private val _profileResponse:MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()
    val profileResponse:LiveData<Resource<ProfileResponse>> get() = _profileResponse
    fun getProfileInfo(customer_id:Int)=viewModelScope.launch {
        _profileResponse.postValue(Resource.Loading())
        _profileResponse.postValue(repository.getProfileInfo(customer_id))
    }

}