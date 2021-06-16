package com.majjane.chefmajjane.views.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentLoginBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.utils.*
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.activities.HomeActivity
import com.majjane.chefmajjane.views.base.BaseFragment


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    private val TAG = "LoginFragment"
    private var callBackManager: CallbackManager? = null
    override fun onStart() {
        super.onStart()
        // TODO: TEST
        if (preferences.getIdCustomer() != -1) {
            requireActivity().startNewActivity(HomeActivity::class.java)
        }
    }

    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewModel.setIntent {
            getGoogleLogIn.launch(it)
        }
        binding.googleSignInBtn.setOnClickListener {
            viewModel.signInWithGoogle(requireActivity())
        }
        binding.facebookSignInBtn.setOnClickListener {
            signInWithFacebook()
        }
        binding.suivantBtn.setOnClickListener {
            if (binding.editTextPhoneNumber.text?.isEmpty() == true) {
                requireView().snackbar(getString(R.string.phone_required))
                return@setOnClickListener
            }
            val phone = binding.editTextPhoneNumber.text.removePrefix("+212 ")
            viewModel.sendOTP(phone.toString().trim())
            Log.d(TAG, "onViewCreated: ${binding.editTextPhoneNumber.text}")
        }


        binding.btnContinueEmail.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signInFragment)
        }

        handlePhoneNumberPrefix()
        observeFacebook()
        observeGoogleLogin()
        observeGConnect()
        observeOTPMessage()
        onBackPressed()

    }

    private fun handlePhoneNumberPrefix() {
        Selection.setSelection(
            binding.editTextPhoneNumber.text,
            binding.editTextPhoneNumber.text.length
        )
        binding.editTextPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!s.toString().contains("+212 ")) {
                    binding.editTextPhoneNumber.setText("+212 ")
                    Selection.setSelection(
                        binding.editTextPhoneNumber.text,
                        binding.editTextPhoneNumber.text.length
                    )
                }
            }
        })
    }

    private fun observeOTPMessage() {
        viewModel.otpResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar3.visible(false)
                    if (it.data.success == 1) {

                        navController.navigate(
                            R.id.action_loginFragment_to_optVerificationFragment,
                            bundleOf("OTP_VERIFICATION" to binding.editTextPhoneNumber.text.toString())
                        )
                        return@observe
                    }
                    if (it.data.success == 0) {
                        requireView().snackbar(getString(R.string.went_wrong))
                    }
                }
                is Resource.Failure -> {
                    requireView().snackbar(getString(R.string.went_wrong))
                    binding.progressBar3.visible(false)
                }
                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        })
    }

    private fun observeFacebook() {
        viewModel.facebookResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar3.visible(false)
                    Log.d(TAG, "onViewCreated: facebook ${it.data}")
                    if (it.data.success == 1) {
                        preferences.saveIdCustomer(it.data.id)
                        requireActivity().startNewActivity(HomeActivity::class.java)
                        return@observe
                    }
                    if (it.data.success == 0) {
                        requireView().snackbar(it.data.message)
                    }

                }
                is Resource.Failure -> {
                    binding.progressBar3.visible(false)
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        })
    }

    private fun observeGConnect() {
        viewModel.gConnectResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar3.visible(false)
                    preferences.saveIdCustomer(it.data.id)
                    requireActivity().startNewActivity(HomeActivity::class.java)
                }
                is Resource.Failure -> {
                    binding.progressBar3.visible(false)
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        })
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"));
        callBackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callBackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                //TODO: Facebook token to api
                viewModel.facebookLogin(result?.accessToken?.token.toString())
                Log.d(TAG, "onSuccess: ${result?.accessToken?.token.toString()}")
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {

            }
        })
    }

    private fun observeGoogleLogin() {
        viewModel.googleLoginResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar3.visible(false)
                    val account = it.data
                    Log.d(TAG, "observeGoogleLogin: ${account.email}")

                    account.email?.let { it1 ->
                        account.familyName?.let { it2 ->
                            account.givenName?.let { it3 ->
                                viewModel.postGoogleLogin(
                                    it1,
                                    it2,
                                    it3,
                                    preferences.getIdLang()
                                )
                            }
                        }
                    }
                }
                is Resource.Failure -> {
                    binding.progressBar3.visible(false)
                    handleApiError(it)
                }
                is Resource.Loading -> {
                    binding.progressBar3.visible(true)
                }
            }
        })
    }

    private val getGoogleLogIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.registerForActivityResult(it)
        }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun createViewModel(): Class<AuthViewModel> = AuthViewModel::class.java


    override fun getFragmentRepository(): AuthRepository =
        AuthRepository(RemoteDataSource().buildApi(AuthApi::class.java))

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager?.onActivityResult(requestCode, resultCode, data)
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