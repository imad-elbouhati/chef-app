package com.majjane.chefmajjane.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val prixFinal: Double,
    val prixTTC: Double,
    val qnt: Int,
    var selectedQuantity: Int = 0,
    val reduction: String
):Parcelable