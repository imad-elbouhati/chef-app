package com.majjane.chefmajjane.viewmodel

import android.accounts.NetworkErrorException
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.majjane.chefmajjane.repository.AuthRepository
import com.majjane.chefmajjane.utils.Resource
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class AuthViewModel(
    val repository: AuthRepository
) : ViewModel() {
    private val TAG = "AuthViewModel"
    private val _googleLoginResponse: MutableLiveData<Resource<GoogleSignInAccount>> =
        MutableLiveData()
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
                // TODO: 5/24/2021 POST VALUES TO API IN VM INSTEAD OF FRGMT
                Log.d(TAG, "handleSignInResult: ${it.email} ${it.id} ${it.displayName}")
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


}