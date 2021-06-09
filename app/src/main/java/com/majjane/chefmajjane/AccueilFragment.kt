package com.majjane.chefmajjane

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.majjane.chefmajjane.adapters.AccueilAdapter
import com.majjane.chefmajjane.databinding.FragmentAccueilBinding
import com.majjane.chefmajjane.network.AccueilMenuApi
import com.majjane.chefmajjane.repository.AccueilMenuRepository
import com.majjane.chefmajjane.responses.AccueilResponseItem
import com.majjane.chefmajjane.utils.Constants.Companion.CATEGORY_BUNDLE
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AccueilMenuViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class AccueilFragment :
    BaseFragment<AccueilMenuViewModel, FragmentAccueilBinding, AccueilMenuRepository>() {
    private val TAG = "AccueilFragment"
    private lateinit var navController: NavController
    private val adapter by lazy {
        AccueilAdapter { category, position -> onCategoryClicked(category, position) }
    }


    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccueilBinding = FragmentAccueilBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        getAccueil()
        binding.recyclerViewAccueil.adapter = adapter


        viewModel.accueilResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar.visible(true)

                }
                is Resource.Success -> {
                    binding.progressBar.visible(false)
                    adapter.setItems(it.data)
                }
                is Resource.Failure -> {
                    binding.progressBar.visible(false)
                    handleApiError(it) {
                        getAccueil()
                    }
                }
            }
        })
    }

    private fun onCategoryClicked(category: AccueilResponseItem, position:Int){
        val bundle = bundleOf(CATEGORY_BUNDLE to category)
        navController.navigate(R.id.action_accueilFragment_to_espaceSushiFragment,bundle)
    }
    private fun getAccueil() {
        viewModel.getAccueil(1)
    }

    override fun createViewModel(): Class<AccueilMenuViewModel> = AccueilMenuViewModel::class.java

    override fun getFragmentRepository(): AccueilMenuRepository = AccueilMenuRepository(
        remoteDataSource.buildApi(
            AccueilMenuApi::class.java
        )
    )

    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            toolbarIcon?.visible(false)
            this.setToolbar("")
        }
    }
}