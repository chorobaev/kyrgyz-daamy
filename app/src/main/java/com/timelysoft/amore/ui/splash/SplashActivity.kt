package com.timelysoft.amore.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.timelysoft.amore.R
import com.timelysoft.amore.extension.getErrors
import com.timelysoft.amore.service.*
import com.timelysoft.amore.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModel<SplashViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        splashViewModel.getRestaurant().observe(this, Observer {restaurants->

            restaurants.doIfError { errorBody ->
                errorBody?.getErrors { msg ->
                    Toast.makeText(this, "", Toast.LENGTH_LONG).show()
                }
            }
            restaurants.doIfNetwork { msg ->
                Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            }
            restaurants.doIfSuccess {
                val intent = Intent(this, MainActivity::class.java)

                val urls = arrayListOf<String>()
                if (it.firstOrNull() != null) {
                    AppPreferences.currencyName = it.first().currency.name
                    AppPreferences.restaurant = it.first().id
                    AppPreferences.bankPay = it.first().onlinePaymentSupported
                    if (it.first().files.isEmpty()) {
                        urls.add("")
                    } else {
                        it.first().files.forEach { file->
                            urls.add(AppPreferences.baseUrl + file.relativeUrl)
                        }
                    }

                }
                intent.putStringArrayListExtra("urls", urls)
                intent.putExtra("restaurant_name", it.first().name)
                Handler().postDelayed({
                    startActivity(intent)
                    finish()
                }, 0)

            }
        })


    }
}