package com.majjane.chefmajjane.views.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentSignUpBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.responses.SignUp
import com.majjane.chefmajjane.utils.Constants.Companion.FROM_CREATE_ACCOUNT
import com.majjane.chefmajjane.utils.Constants.Companion.PHONE_NUMBER_KEY
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.utils.startNewActivity
import com.majjane.chefmajjane.utils.visible
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class SignUpFragment : BaseFragment<AuthViewModel, FragmentSignUpBinding, AuthRepository>() {
    private lateinit var navController: NavController
    private var isFromCreateAccount = false
    private var phoneNumberArg = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bundle.getBoolean(FROM_CREATE_ACCOUNT).let {
                isFromCreateAccount = it
            }
            bundle.getString(PHONE_NUMBER_KEY)?.let {
                phoneNumberArg = it
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        if (isFromCreateAccount) {
            binding.phoneNumber.visible(true)
        } else {
            binding.email.visible(false)
        }

        binding.nextButton.setOnClickListener {
            preformSignUp()
        }

        binding.backArrow.setOnClickListener {
           findNavController().popBackStack()
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
            if (isFromCreateAccount) {
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
            } else {
                if (prenom.getText().isEmpty() || nom.getText().isEmpty() || password.getText()
                        .isEmpty()
                ) {
                    requireView().snackbar(getString(R.string.empty_field_not_allowed))
                    return
                }

            }


            if (isFromCreateAccount) {
                viewModel.signUp(
                    SignUp(
                        email.getText(),
                        prenom.getText(),
                        preferences.getIdLang(),
                        nom.getText(),
                        password.getText(),
                        binding.phoneNumber.getText()
                    )
                )
            } else {
                viewModel.signUpWithPhone(
                    SignUp(
                        "",
                        prenom.getText(),
                        preferences.getIdLang(),
                        nom.getText(),
                        password.getText(),
                        phoneNumberArg
                    )
                )
            }

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