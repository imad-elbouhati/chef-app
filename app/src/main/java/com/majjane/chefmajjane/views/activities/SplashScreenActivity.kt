package com.majjane.chefmajjane.views.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.utils.startNewActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            this.startNewActivity(LoginActivity::class.java)
        }, 2000)
    }
}