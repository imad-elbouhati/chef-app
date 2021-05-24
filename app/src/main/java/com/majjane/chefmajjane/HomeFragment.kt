package com.majjane.chefmajjane

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.databinding.FragmentHomeBinding
import com.majjane.chefmajjane.network.CategoryApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.CategoryRepository
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.CategoryViewModel
import com.majjane.chefmajjane.views.base.BaseFragment


class HomeFragment : BaseFragment<CategoryViewModel,FragmentHomeBinding,CategoryRepository>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun createViewModel(): Class<CategoryViewModel> = CategoryViewModel::class.java

    override fun getFragmentRepository(): CategoryRepository =CategoryRepository(RemoteDataSource().buildApi(CategoryApi::class.java))

}