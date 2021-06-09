package com.majjane.chefmajjane.viewmodel

import android.app.Activity
import android.content.Intent
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
import com.majjane.chefmajjane.responses.login.GoogleResponse
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {
    private val TAG = "AuthViewModel"
    private val _googleLoginResponse: MutableLiveData<Resource<GoogleSignInAccount>> = MutableLiveData()
    private val _gConnectResponse: MutableLiveData<Resource<GoogleResponse>> = MutableLiveData()
    private val _facebookResponse: MutableLiveData<Resource<Int>> = MutableLiveData()
    val facebookResponse: MutableLiveData<Resource<Int>> get() = _facebookResponse
    val gConnectResponse: LiveData<Resource<GoogleResponse>> get() = _gConnectResponse
    val googleLoginResponse: LiveData<Resource<GoogleSignInAccount>> get() = _googleLoginResponse
    private var onIntentListener: ((Intent) -> Unit?)? = null
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
            when(e){
                is ApiException ->{
                    Resource.Failure(false,e.statusCode,null)
                }
                is IOException ->{
                    Resource.Failure(true,null,null)
                }
            }
            //updateUI(null)
        }
    }

    fun postGoogleLogin(email: String, familyName: String, givenName: String,id_lang:Int) = viewModelScope.launch {
        _gConnectResponse.postValue(Resource.Loading())
       _gConnectResponse.postValue(repository.postGoogleLogin(email,familyName,givenName,id_lang))
    }

    fun facebookLogin(id_lang: Int, accessToken: String) = viewModelScope.launch{
        _facebookResponse.postValue(repository.facebookLogin(id_lang,accessToken))
    }


}