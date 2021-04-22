package io.flaterlab.kyrgyzdaamy.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.databinding.ActivityMainBinding
import io.flaterlab.kyrgyzdaamy.extension.setupWithNavController
import org.koin.android.ext.android.bind
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private var currentNavController: LiveData<NavController>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
            R.navigation.search_navigation,
            R.navigation.profile_navigation
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

    fun hideBottomNav(){
        binding.bottomNav.visibility = View.GONE
    }
    fun showBottomNav(){
        binding.bottomNav.visibility = View.VISIBLE
    }
}
