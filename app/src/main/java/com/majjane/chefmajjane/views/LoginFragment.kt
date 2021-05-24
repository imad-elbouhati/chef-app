package com.majjane.chefmajjane.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.majjane.chefmajjane.views.base.BaseFragment
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.FragmentLoginBinding
import com.majjane.chefmajjane.network.AuthApi
import com.majjane.chefmajjane.network.RemoteDataSource
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.utils.Resource
import com.majjane.chefmajjane.utils.handleApiError
import com.majjane.chefmajjane.utils.snackbar
import com.majjane.chefmajjane.viewmodel.AuthViewModel


class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    private val TAG = "LoginFragment"
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
        observeGoogleLogin()
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
}