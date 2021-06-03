package com.majjane.chefmajjane.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccueilResponseItem(
    val id: Int,
    val id_menu: Int,
    val image: String,
    val name: String
):Parcelable