package com.majjane.chefmajjane.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentMyProfileBinding
import com.majjane.chefmajjane.network.ProfileApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.ProfileRepository
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.ProfileViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class MyProfileFragment :
    BaseFragment<ProfileViewModel, FragmentMyProfileBinding, ProfileRepository>() {
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).apply {
            setToolbar(getString(R.string.my_profile))
            bigCircleImageView?.visible(false)
            setToolbarHeight(277)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyProfileBinding = FragmentMyProfileBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun getFragmentRepository(): ProfileRepository = ProfileRepository(
        RemoteDataSource().buildApi(
            ProfileApi::class.java
        )
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProfileInfo(preferences.getIdCustomer())

        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar6.visible(false)
                    binding.nom.setText(it.data.last_name)
                    binding.prenom.setText(it.data.last_name)
                    binding.email.setText(it.data.email)
                    it.data.phone_number?.let { phoneNumber->
                        binding.numTele.setText(phoneNumber)
                    }

                }
                is Resource.Failure -> {
                    binding.progressBar6.visible(false)
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressBar6.visible(true)
                }
            }
        })
    }

}