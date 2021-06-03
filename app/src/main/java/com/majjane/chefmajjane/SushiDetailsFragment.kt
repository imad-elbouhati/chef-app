package com.majjane.chefmajjane

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.databinding.FragmentSushiDetailsBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.views.base.BaseFragment


class SushiDetailsFragment :
    BaseFragment<AccueilMenuViewModel, FragmentSushiDetailsBinding, AccueilMenuRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSushiDetailsBinding = FragmentSushiDetailsBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java

    override fun getFragmentRepository(): AccueilMenuRepository = AccueilMenuRepository(
        RemoteDataSource().buildApi(AccueilMenuApi::class.java)
    )
}