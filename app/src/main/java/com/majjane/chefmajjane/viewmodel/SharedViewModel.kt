package com.majjane.chefmajjane.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.majjane.chefmajjane.responses.AccueilResponseItem
import com.majjane.chefmajjane.responses.Address
import com.majjane.chefmajjane.responses.Article

class SharedViewModel: ViewModel() {

    val address:MutableLiveData<Address> by lazy {
        MutableLiveData<Address>()
    }
    val commandList:MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>()
    }

    val sharedCategory:MutableLiveData<AccueilResponseItem> by lazy {
        MutableLiveData<AccueilResponseItem>()
    }
}
