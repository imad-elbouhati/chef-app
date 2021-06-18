package com.majjane.chefmajjane.repository

import com.majjane.chefmajjane.network.CommandeApi
import com.majjane.chefmajjane.repository.base.BaseRepository
import com.majjane.chefmajjane.responses.model.CommandeModel

class CommandeRepository(val api: CommandeApi) : BaseRepository() {
    suspend fun getCitiesList(idLang: Int, idCustomer: Int) = safeApiCall {
            api.getCitiesList(idLang,idCustomer)
    }

    suspend fun sendCommandeToApi(commandeModel: CommandeModel) =safeApiCall  {
            api.sendCommandeToApi(commandeModel)
    }
}