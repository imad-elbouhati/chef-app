package com.majjane.chefmajjane

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.majjane.chefmajjane.databinding.FragmentChangePasswordBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.responses.Password
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment

class ChangePasswordFragment :
    BaseFragment<AuthViewModel, FragmentChangePasswordBinding, AuthRepository>() {
    private lateinit var navController: NavController
    override fun onResume() {
        super.onResume()
        ((activity) as HomeActivity).setToolbar(getString(R.string.change_password))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.saveButton.setOnClickListener {
            updatePassword()
        }
        binding.cancelButton.setOnClickListener {
            navController.navigate(R.id.action_changePasswordFragment_to_homeFragment)
        }

        ((activity) as HomeActivity).toolbarIcon?.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.updatePasswordResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.progressBar7.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar7.visible(false)
                    requireView().snackbar(getString(R.string.went_wrong))

                }
                is Resource.Success -> {
                    if(it.data.success == 1){
                        requireView().snackbar(getString(R.string.password_updated_succefully))
                        navController.navigate(R.id.action_changePasswordFragment_to_homeFragment)
                        return@observe
                    }
                    if(it.data.success == 0){
                        requireView().snackbar(it.data.message)
                    }
                }
            }
        })
    }

    private fun updatePassword() {
        binding.apply {
            if (enterPassword.getText().isEmpty() || newPassword.getText()
                    .isEmpty() || reTypeNewPassword.getText().isEmpty()
            ) {
                requireView().snackbar(getString(R.string.empty_field_not_allowed))
                return
            }
            if (newPassword.getText() != reTypeNewPassword.getText()) {
                requireView().snackbar(getString(R.string.password_do_not_match))
                return
            }

            viewModel.updatePassword(
                Password(
                    preferences.getIdCustomer(),
                    newPassword.getText(),
                    enterPassword.getText()
                )
            )
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChangePasswordBinding =
        FragmentChangePasswordBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(RemoteDataSource().buildApi(AuthApi::class.java))

}