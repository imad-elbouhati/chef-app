package com.majjane.chefmajjane.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.repository.CategoryRepository
import com.majjane.chefmajjane.repository.base.BaseRepository
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthRepository) as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel(repository as CategoryRepository) as T
            else -> throw IllegalArgumentException("viewModel Not Found")
        }
    }
}