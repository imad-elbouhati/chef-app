package com.majjane.chefmajjane.views

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.majjane.chefmajjane.R


class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController:NavController = Navigation.findNavController(view)
        Handler().postDelayed({
            navController.navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }, 3000)
    }
}