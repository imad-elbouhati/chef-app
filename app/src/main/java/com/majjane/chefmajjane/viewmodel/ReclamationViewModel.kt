package com.majjane.chefmajjane.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majjane.chefmajjane.repository.ReclamationRepository
import com.majjane.chefmajjane.responses.ReclamationResponse
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ReclamationViewModel(private val repository: ReclamationRepository) : ViewModel() {

    private val _reclamationLiveData: MutableLiveData<Resource<ReclamationResponse>> =
        MutableLiveData()
    val reclamationLiveData: LiveData<Resource<ReclamationResponse>> get() = _reclamationLiveData

}