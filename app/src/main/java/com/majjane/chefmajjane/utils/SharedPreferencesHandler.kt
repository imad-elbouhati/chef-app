package com.majjane.chefmajjane.utils

import android.content.Context
import com.majjane.chefmajjane.MainApplication
import com.majjane.chefmajjane.utils.Constants.Companion.EMAIL_KEY
import com.majjane.chefmajjane.utils.Constants.Companion.FAMILY_NAME_KEY
import com.majjane.chefmajjane.utils.Constants.Companion.ID_CUSTOMER_KEY
import com.majjane.chefmajjane.utils.Constants.Companion.ID_LANG_KEY
import com.majjane.chefmajjane.utils.Constants.Companion.MY_DATA_PREFERENCES
import com.majjane.chefmajjane.utils.Constants.Companion.NAME_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SharedPreferencesHandler(val context:Context) {

    private val sharedPreference = context.getSharedPreferences(MY_DATA_PREFERENCES, Context.MODE_PRIVATE)
    private val editor = sharedPreference.edit()

    fun saveIdCustomer(id_customer: Int) {
        editor.apply {
            putInt(ID_CUSTOMER_KEY, id_customer)
            commit()
        }
    }

    fun getIdCustomer(): Int {
        return sharedPreference.getInt(ID_CUSTOMER_KEY, 0)
    }

    fun saveIdLang(id_lang: Int) {
        editor.apply {
            putInt(ID_LANG_KEY, id_lang)
            commit()
        }
    }

    fun getIdLang(): Int {
        return sharedPreference.getInt(ID_LANG_KEY, 1)
    }
}