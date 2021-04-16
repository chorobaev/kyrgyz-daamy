package io.flaterlab.kyrgyzdaamy.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.extension.setupWithNavController

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        BasketCommands.sumOfBasket.observe(this, {
            val badge = bottomNavigationView.getOrCreateBadge(R.id.basket_navigation)
            badge.number = it
            badge.maxCharacterCount = 10
            badge.isVisible = true

        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navGraphIds = listOf(
            R.navigation.mobile_navigation,
            R.navigation.basket_navigation,
            R.navigation.news_navigation,
            R.navigation.search_navigation
        )
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
