package com.majjane.chefmajjane.network

import com.majjane.chefmajjane.responses.AccueilResponse
import com.majjane.chefmajjane.responses.FoodResponse
import com.majjane.chefmajjane.responses.menu.MenuResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AccueilMenuApi {
    @GET("Accueil.php")
    suspend fun getAccueil(
        @Query("id_lang") id_lang: Int
    ): AccueilResponse

    @GET("getProductsByCategory.php")
    suspend fun getProductsByCategory(
        @Query("id_lang") idLang: Int,
        @Query("id") idCategory: Int,
        @Query("i") indexDepart: Int
    ): Response<FoodResponse>

    @GET("Menu.php")
    suspend fun getMenuList(@Query("id_lang") idLang: Int, @Query("id") idMenu: Int): MenuResponse

    @GET("getProductsByCategory.php")
    suspend fun getFoodByMenuList(
        @Query("id_lang") idLang: Int,
        @Query("id") idMenu: Int,
        @Query("i") searchFoodPage: Int
    ): Response<FoodResponse>
}