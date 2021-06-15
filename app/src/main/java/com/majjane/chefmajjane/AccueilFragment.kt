package com.majjane.chefmajjane

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
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
import com.majjane.chefmajjane.viewmodel.SharedViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class AccueilFragment :
    BaseFragment<AccueilMenuViewModel, FragmentAccueilBinding, AccueilMenuRepository>() {
    private val TAG = "AccueilFragment"
    private lateinit var navController: NavController
    lateinit var sharedViewModel: SharedViewModel

    private val adapter by lazy {
        AccueilAdapter { category, position -> onCategoryClicked(category, position) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        }
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

        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_profileMenuFragment)
        }


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
        onBackPressed()
    }

    private fun onCategoryClicked(category: AccueilResponseItem, position: Int) {
        // val bundle = bundleOf(CATEGORY_BUNDLE to category)
        sharedViewModel.sharedCategory.value = category
        navController.navigate(R.id.action_accueilFragment_to_espaceSushiFragment)
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
            toolbarIcon?.setImageResource(R.drawable.profile_ic)
            setToolbarHeight(50)
            this.setToolbar("")
        }
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}