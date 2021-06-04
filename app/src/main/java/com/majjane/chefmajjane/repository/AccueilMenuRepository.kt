package com.majjane.chefmajjane.repository

import android.util.Log
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.responses.menu.MenuResponse
import com.majjane.chefmajjane.utils.Resource
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

    suspend fun getMenuList(idLang: Int, idMenu: Int) = safeApiCall {
        api.getMenuList(idLang,idMenu)
    }

    suspend fun getFoodByMenuList(idLang: Int, idMenu: Int, searchFoodPage: Int) = api.getFoodByMenuList(idLang, idMenu, searchFoodPage)


}