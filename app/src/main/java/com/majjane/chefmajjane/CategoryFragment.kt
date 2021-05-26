package com.majjane.chefmajjane

import android.R
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.majjane.chefmajjane.databinding.FragmentCategoryBinding
import com.majjane.chefmajjane.network.CategoryApi
import com.majjane.chefmajjane.repository.CategoryRepository
import com.majjane.chefmajjane.viewmodel.CategoryViewModel
import com.majjane.chefmajjane.views.base.BaseFragment


class CategoryFragment : BaseFragment<CategoryViewModel, FragmentCategoryBinding, CategoryRepository>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdk = Build.VERSION.SDK_INT

    }
    override fun createViewModel(): Class<CategoryViewModel> = CategoryViewModel::class.java

    override fun getFragmentRepository(): CategoryRepository = CategoryRepository(
        remoteDataSource.buildApi(
            CategoryApi::class.java
        )
    )

}