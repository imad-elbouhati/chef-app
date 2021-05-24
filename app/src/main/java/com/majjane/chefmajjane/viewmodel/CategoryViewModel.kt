package com.majjane.chefmajjane.viewmodel

import androidx.lifecycle.ViewModel
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.repository.CategoryRepository

class CategoryViewModel(
    private val repository: CategoryRepository
): ViewModel() {
}