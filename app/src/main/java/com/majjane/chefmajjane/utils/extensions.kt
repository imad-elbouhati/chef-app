package com.majjane.chefmajjane.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.views.LoginFragment


fun <A : Activity> Activity.startNewActivity(activity: Class<A>) {
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible:Boolean){
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enable:Boolean){
    isEnabled = enable
    alpha = if (enable) 1f else 0.5f
}
fun View.snackbar(message: String, action:(()->Unit)?=null){
    val snackbar = Snackbar.make(this,message,Snackbar.LENGTH_SHORT)
    action?.let {
        snackbar.setAction(R.string.ressayez){
            it()
        }
    }
    snackbar.show()
}
fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (()->Unit)?=null
){
    when{
        failure.isNetwork -> requireView().snackbar(
            R.string.verifier_connextion.toString(),
            retry
        )
        failure.errorCode == 401 -> {
            if(this is LoginFragment){
                requireView().snackbar(R.string.email_incorrect.toString())
            }else{
              // TODO: 5/24/2021 Logout functionality
            }
        }else->{
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}
