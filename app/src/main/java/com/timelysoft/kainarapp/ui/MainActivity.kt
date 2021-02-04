package com.timelysoft.kainarapp.ui


import android.graphics.Color
import android.os.Bundle

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.extension.setupWithNavController
import com.timelysoft.kainarapp.ui.food.FoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        BasketCommands.sumOfBasket.observe(this, Observer {
            val badge = bottomNavigationView.getOrCreateBadge(R.id.basket_navigation)
            badge.number = it
            badge.maxCharacterCount = 10
            badge.isVisible = true

        })


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navGraphIds = listOf(R.navigation.mobile_navigation, R.navigation.basket_navigation, R.navigation.news_navigation)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_container,
            intent = intent
        )
        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }



}
