package com.majjane.chefmajjane.views.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentSignInViaEmailBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.utils.Constants.Companion.FROM_CREATE_ACCOUNT
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.startNewActivity
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class SignInViaEmailFragment : BaseFragment<AuthViewModel, FragmentSignInViaEmailBinding, AuthRepository>() {
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        //log in click listener
        binding.connecterButton.setOnClickListener {
            preformSignIn()
        }
        //back arrow click listener
        binding.backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        //Créer un compte click listener
        binding.creeUnCompteTextView.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment2,
            bundleOf(FROM_CREATE_ACCOUNT to true))
        }


        //observe login response
        viewModel.loginResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar4.visible(false)
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
                    binding.progressBar4.visible(true)
                }
                is Resource.Failure -> {
                    binding.progressBar4.visible(false)
                    requireView().snackbar(getString(R.string.went_wrong))
                }
            }
        })
    }

    private val TAG = "SignInFragment"
    private fun preformSignIn() {
        binding.apply {
            if (emailTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
                requireView().snackbar(getString(R.string.empty_field_not_allowed))
                return
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailTextField.getText()).matches()) {
                requireView().snackbar(getString(R.string.invalid_email))
                return
            }
            viewModel.logInWithEmail(Login(0,"","",emailTextField.getText(),passwordTextField.getText()))
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInViaEmailBinding = FragmentSignInViaEmailBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AuthViewModel> = AuthViewModel::class.java


    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(RemoteDataSource().buildApi(AuthApi::class.java))


}