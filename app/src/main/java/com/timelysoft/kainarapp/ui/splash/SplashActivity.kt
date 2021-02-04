package com.timelysoft.kainarapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.extension.getErrors
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.doIfError
import com.timelysoft.kainarapp.service.doIfNetwork
import com.timelysoft.kainarapp.service.doIfSuccess
import com.timelysoft.kainarapp.service.model2.AuthBody
import com.timelysoft.kainarapp.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity(){

    private val splashViewModel : SplashViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val authBody = AuthBody("Admin", "12qw!@QW")

        if (AppPreferences.accessToken != null) {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 3000)
        } else {
            splashViewModel.sendAuthCredentials(authBody).observe(this, Observer { response ->
                response.doIfSuccess {
                    it?.let { token ->
                        AppPreferences.restaurant = "c7b1a0d1-6928-4512-f859-08d8c13995d7"
                        AppPreferences.accessToken = token.accessToken
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }

                response.doIfError {
                    it?.let {
                        it.getErrors { msg ->
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                response.doIfNetwork {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }


            })
        }

        /*
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
         */
    }
}