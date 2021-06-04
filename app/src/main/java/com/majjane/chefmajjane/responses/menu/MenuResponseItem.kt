package com.majjane.chefmajjane.responses.menu

data class MenuResponseItem(
    val id: Int,
    val libelle: String,
    var selected:Boolean = false
)