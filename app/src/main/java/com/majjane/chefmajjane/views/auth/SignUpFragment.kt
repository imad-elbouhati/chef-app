package com.majjane.chefmajjane.views.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentSignUpBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.responses.SignUp
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.startNewActivity
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class SignUpFragment : BaseFragment<AuthViewModel, FragmentSignUpBinding, AuthRepository>() {
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.nextButton.setOnClickListener {
            preformSignUp()
        }

        binding.backArrow.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment2_to_loginFragment)
        }

        viewModel.signUpResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar8.visible(false)
                    if (it.data.success == 1) {
                        preferences.saveIdCustomer(it.data.id)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                        return@observe
                    }
                    if (it.data.success == 0) {
                        requireView().snackbar(it.data.message)
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar8.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar8.visible(false)
                    requireView().snackbar(getString(R.string.empty_field_not_allowed))
                }
            }
        })
    }

    private fun preformSignUp() {
        binding.apply {
            if (prenom.getText().isEmpty() || nom.getText().isEmpty() || email.getText()
                    .isEmpty() || password.getText().isEmpty()
            ) {
                requireView().snackbar(getString(R.string.empty_field_not_allowed))
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                requireView().snackbar(getString(R.string.invalid_email))
                return
            }
            viewModel.signUp(
                SignUp(
                    email.getText(),
                    prenom.getText(),
                    preferences.getIdLang(),
                    nom.getText(),
                    password.getText(),
                    ""
                )
            )
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AuthViewModel> = AuthViewModel::class.java

    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(RemoteDataSource().buildApi(AuthApi::class.java))
}