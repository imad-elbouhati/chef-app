package com.majjane.chefmajjane.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.AccueilResponse
import com.majjane.chefmajjane.responses.FoodResponse
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AccueilMenuViewModel(
    private val repositoryAccueil: AccueilMenuRepository
) : ViewModel() {
    private val _accueilResponse: MutableLiveData<Resource<AccueilResponse>> = MutableLiveData()
    private val _foodListResponse: MutableLiveData<Resource<FoodResponse>> = MutableLiveData()
    val foodListResponse: LiveData<Resource<FoodResponse>> get() = _foodListResponse
    private var nextFoodListResponse: FoodResponse?=null
    val accueilResponse: LiveData<Resource<AccueilResponse>> get() = _accueilResponse
    var searchFoodPage = 0
    fun getAccueil(
        lang_id: Int
    ) = viewModelScope.launch {
        _accueilResponse.postValue(Resource.Loading())
        _accueilResponse.postValue(repositoryAccueil.getAccueil(lang_id))
    }

    init {
        getFoodList(idLang = 1,121)
    }
    fun getFoodList(
        idLang: Int,
        idCategory: Int,
    ) = viewModelScope.launch {
        _foodListResponse.postValue(Resource.Loading())
        val response = repositoryAccueil.getProductsByCategory(idLang, idCategory, searchFoodPage)
        try {
            _foodListResponse.postValue(handleFoodListResponse(response))
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> Resource.Failure(
                    false,
                    throwable.code(),
                    throwable.response()?.errorBody()
                )
                else -> {
                    Resource.Failure(true, null, null)
                }
            }

        }
    }
    private  val TAG = "AccueilMenuViewModel"
    private fun handleFoodListResponse(response: Response<FoodResponse>): Resource<FoodResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchFoodPage+=10
                Log.d(TAG, "handleFoodListResponse: $searchFoodPage")
                if(nextFoodListResponse==null){
                    nextFoodListResponse = it
                    Log.d(TAG, "handleFoodListResponse: first time $nextFoodListResponse")
                }else{
                    val oldData = nextFoodListResponse?.articles as MutableList
                    Log.d(TAG, "handleFoodListResponse: second time $nextFoodListResponse")
                    val newData = it.articles
                    Log.d(TAG, "handleFoodListResponse: new Data ${newData.size}")
                    oldData.addAll(newData)
                }
                return Resource.Success(nextFoodListResponse ?:it)
            }
        }
        return Resource.Failure(false,null,null)
    }


}
