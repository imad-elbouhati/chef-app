package com.majjane.chefmajjane.views

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentMyProfileBinding
import com.majjane.chefmajjane.network.ProfileApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.ProfileRepository
import com.majjane.chefmajjane.responses.ProfileResponse
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.ProfileViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class MyProfileFragment :
    BaseFragment<ProfileViewModel, FragmentMyProfileBinding, ProfileRepository>() {
    private lateinit var navController:NavController
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
        setProfileInfo()
        binding.enregister.setOnClickListener {
            updateProfileInfo()
        }

        navController = Navigation.findNavController(view)
        viewModel.updateProfileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar6.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar6.visible(false)

                    handleApiError(it)
                }
                is Resource.Success -> {
                    binding.progressBar6.visible(false)
                    if (it.data.success == 1) {
                        requireView().snackbar(getString(R.string.profile_updated))
                        return@observe
                    }
                    if (it.data.success == 0) {
                        requireView().snackbar(it.data.message)
                    }
                }
            }
        })

        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            navController.navigate(R.id.action_myProfileFragment_to_homeFragment)
        }
    }

    private fun updateProfileInfo() {
        binding.apply {
            if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText()
                    .isEmpty() || numTele.getText().isEmpty()
            ) {
                requireView().snackbar(getString(R.string.empty_field_not_allowed))
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                email.setError(getString(R.string.invalid_email))
                return
            }
            if (!TextUtils.isDigitsOnly(numTele.getText())) {
                numTele.setError(getString(R.string.invalid_phone))
                return
            }
            viewModel.updateProfile(
                ProfileResponse(
                    preferences.getIdCustomer(),
                    nom.getText(),
                    prenom.getText(),
                    email.getText(),
                    numTele.getText()
                )
            )
        }
    }

    private fun setProfileInfo() {
        viewModel.profileResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar6.visible(false)
                    binding.nom.setText(it.data.last_name)
                    binding.prenom.setText(it.data.last_name)
                    binding.email.setText(it.data.email)
                    it.data.phone_number?.let { phoneNumber ->
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