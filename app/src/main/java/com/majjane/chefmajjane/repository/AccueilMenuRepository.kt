package com.majjane.chefmajjane.repository

import android.util.Log
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.repository.base.BaseRepository
import retrofit2.http.Query

class AccueilMenuRepository(val api: AccueilMenuApi) : BaseRepository() {

    private val TAG = "AccueilMenuRepository"

    suspend fun getAccueil(id_lang: Int) = safeApiCall {
        api.getAccueil(id_lang)
    }

    suspend fun getProductsByCategory(
        idLang: Int,
        idCategory: Int,
        indexDepart: Int
    ) = api.getProductsByCategory(idLang, idCategory, indexDepart)


}