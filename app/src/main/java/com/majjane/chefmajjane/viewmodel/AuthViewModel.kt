package com.majjane.chefmajjane.viewmodel

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.responses.BaseResponse
import com.majjane.chefmajjane.responses.Password
import com.majjane.chefmajjane.responses.SignUp
import com.majjane.chefmajjane.responses.login.GoogleResponse
import com.majjane.chefmajjane.responses.login.Login
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {
    private val TAG = "AuthViewModel"
    private val _googleLoginResponse: MutableLiveData<Resource<GoogleSignInAccount>> =
        MutableLiveData()
    private val _gConnectResponse: MutableLiveData<Resource<GoogleResponse>> = MutableLiveData()
    private val _facebookResponse: MutableLiveData<Resource<Int>> = MutableLiveData()
    private val _signUpResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    private val _loginResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    private var onIntentListener: ((Intent) -> Unit?)? = null
    private val _otpResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    private val _updatePasswordResponse: MutableLiveData<Resource<BaseResponse>> = MutableLiveData()
    val facebookResponse: MutableLiveData<Resource<Int>> get() = _facebookResponse
    val gConnectResponse: LiveData<Resource<GoogleResponse>> get() = _gConnectResponse
    val googleLoginResponse: LiveData<Resource<GoogleSignInAccount>> get() = _googleLoginResponse
    val signUpResponse: LiveData<Resource<BaseResponse>> get() = _signUpResponse

    val loginResponse: LiveData<Resource<BaseResponse>> get() = _loginResponse

    val otpResponse: LiveData<Resource<BaseResponse>> get() = _otpResponse
    val updatePasswordResponse: LiveData<Resource<BaseResponse>> get() = _updatePasswordResponse

    private val _verificationOtpResponse: MutableLiveData<Resource<BaseResponse>> =
        MutableLiveData()
    val verificationOtpResponse: LiveData<Resource<BaseResponse>> get() = _verificationOtpResponse

    fun setIntent(onIntent: (Intent) -> Unit) {
        onIntentListener = onIntent
    }

    fun signInWithGoogle(activity: Activity) {
        viewModelScope.launch {
            val mGoogleSignInClient: GoogleSignInClient
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            onIntentListener?.let { it(signInIntent) }
        }
    }

    fun registerForActivityResult(it: ActivityResult?) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(
            it?.data
        )
        handleSignInResult(task)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                _googleLoginResponse.postValue(Resource.Success(it))
                // TODO: 5/24/2021 POST VALUES TO API HERE INSTEAD OF FRGMT
            }
        } catch (e: Exception) {
            when (e) {
                is ApiException -> {
                    Resource.Failure(false, e.statusCode, null)
                }
                is IOException -> {
                    Resource.Failure(true, null, null)
                }
            }
            //updateUI(null)
        }
    }

    fun postGoogleLogin(email: String, familyName: String, givenName: String, id_lang: Int) =
        viewModelScope.launch {
            _gConnectResponse.postValue(Resource.Loading())
            _gConnectResponse.postValue(
                repository.postGoogleLogin(
                    email,
                    familyName,
                    givenName,
                    id_lang
                )
            )
        }

    fun facebookLogin(id_lang: Int, accessToken: String) = viewModelScope.launch {
        _facebookResponse.postValue(repository.facebookLogin(id_lang, accessToken))
    }

    fun sendOTP(phoneNumber: String) = viewModelScope.launch {
        _otpResponse.postValue(Resource.Loading())
        _otpResponse.postValue(repository.sendOTP(phoneNumber))
    }

    fun verifyOTP(code: String, phoneNumberArg: String) = viewModelScope.launch {
        _verificationOtpResponse.postValue(Resource.Loading())
        _verificationOtpResponse.postValue(repository.verifyOTP(code, phoneNumberArg))
    }

    fun signUp(signUp: SignUp) = viewModelScope.launch {
        _signUpResponse.postValue(Resource.Loading())
        _signUpResponse.postValue(repository.signUp(signUp))
    }

    fun logInWithEmail(login: Login) = viewModelScope.launch {
        _loginResponse.postValue(Resource.Loading())
        _loginResponse.postValue(repository.loginWithEmail(login))
    }

    fun updatePassword(password: Password) = viewModelScope.launch {
        Log.d(TAG, "updatePassword: $password")
        _updatePasswordResponse.postValue(Resource.Loading())
        _updatePasswordResponse.postValue(repository.updatePassword(password))
    }


}