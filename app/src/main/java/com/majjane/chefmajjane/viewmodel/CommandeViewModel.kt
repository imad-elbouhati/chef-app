package com.majjane.chefmajjane.viewmodel

import android.widget.ResourceCursorAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majjane.chefmajjane.repository.CommandeRepository
import com.majjane.chefmajjane.responses.BaseResponse
import com.majjane.chefmajjane.responses.cityresponse.CityDeliveryPriceResponse
import com.majjane.chefmajjane.responses.model.CommandeModel
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch

class CommandeViewModel(val repository: CommandeRepository) : ViewModel() {

    val citiesListResponse: MutableLiveData<Resource<CityDeliveryPriceResponse>> = MutableLiveData()
    val confirmCommandResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    fun getCitiesList(idLang: Int, idCustomer: Int) = viewModelScope.launch {
        citiesListResponse.postValue(Resource.Loading())
        citiesListResponse.postValue(repository.getCitiesList(idLang, idCustomer))
    }

    fun sendCommandeToApi(commandeModel: CommandeModel) = viewModelScope.launch {
        confirmCommandResponse.postValue(Resource.Loading())
        confirmCommandResponse.postValue(repository.sendCommandeToApi(commandeModel))

    }


}