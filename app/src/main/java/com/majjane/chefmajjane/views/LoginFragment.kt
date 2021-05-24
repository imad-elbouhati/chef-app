package com.majjane.chefmajjane.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.majjane.chefmajjane.databinding.FragmentLoginBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.viewmodel.AuthViewModel
import com.majjane.chefmajjane.views.base.BaseFragment
import java.util.*


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    private val TAG = "LoginFragment"
    private var callBackManager:CallbackManager ?=null
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        Log.d(TAG, "onStart: ${account?.email} ${account?.displayName}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setIntent {
            getGoogleLogIn.launch(it)
        }
        binding.googleSignInBtn.setOnClickListener {
            viewModel.signInWithGoogle(requireActivity())
        }
        binding.facebookSignInBtn.setOnClickListener {
           signInWithFacebook()
        }
        observeGoogleLogin()
    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"));
        callBackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callBackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(TAG, "onSuccess: ${result?.accessToken?.token}")
                //TODO: Facebook token to api
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel: ")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, "onError: ${error.toString()}")

            }
        })
    }

    private fun observeGoogleLogin() {
        viewModel.googleLoginResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Success -> {
                    requireView().snackbar("Google Login Succeed ${it.data.email}")
                    // TODO: 5/24/2021 POST values to API
                }
                is Resource.Failure -> {
                    handleApiError(it)
                }
            }
        })
    }

    private val getGoogleLogIn = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
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
}