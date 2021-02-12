package com.timelysoft.amore.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.timelysoft.amore.R
import com.timelysoft.amore.ui.MainActivity


class SplashActivity : AppCompatActivity(){

   // private val splashViewModel : SplashViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

       Handler().postDelayed({
           startActivity(Intent(this, MainActivity::class.java))
           finish()
       }, 3000)


    }
}