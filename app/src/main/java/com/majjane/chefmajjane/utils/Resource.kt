package com.majjane.chefmajjane.utils

import okhttp3.ResponseBody

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val isNetwork: Boolean, val errorCode: Int?, val errorBody: ResponseBody?) : Resource<Nothing>()
    class Loading<T> : Resource<T>()
}