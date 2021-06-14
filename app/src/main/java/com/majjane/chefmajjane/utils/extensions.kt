package com.majjane.chefmajjane.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.views.auth.LoginFragment


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
    val snackbar = Snackbar.make(this,message,5000)
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
            getString(R.string.verifier_connextion),
            retry
        )
        failure.errorCode == 401 -> {
            if(this is LoginFragment){
                requireView().snackbar(getString(R.string.email_or_password_incorrect))
            }else{
              // TODO: 5/24/2021 Logout functionality
            }
        }else->{
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

