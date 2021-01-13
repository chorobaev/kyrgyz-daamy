package com.timelysoft.kainarapp.ui

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.timelysoft.kainarapp.R
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.RetrofitClient
import com.timelysoft.kainarapp.service.model.AuthModel
import kotlinx.android.synthetic.main.activity_main.*

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModel()

    companion object {
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var menu: MenuItem
        fun openDrawer() {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        fun showSignOutButton(isVisible: Boolean) {
            menu.isVisible = isVisible
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.GONE
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        //viewModel.clearBasket()
        updateToken()


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        menu = navView.menu.getItem(6)

        showSignOutButton(AppPreferences.isLogined)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_out -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.out)
                    builder.setMessage(R.string.confirm_sign_out)
                    builder.setPositiveButton(R.string.sign_out) { _, _ ->
                        AppPreferences.clear()
                        showSignOutButton(false)
                        drawerLayout.closeDrawer(Gravity.LEFT)
                        navController.navigate(R.id.nav_home)
                    }

                    builder.setNegativeButton(R.string.cancel) { _, _ ->

                    }
                    val dialog = builder.create();
                    dialog.setOnShowListener {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setBackgroundColor(Color.TRANSPARENT)
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setBackgroundColor(Color.TRANSPARENT)
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
                    }

                    dialog.show()
                    false
                }
                else -> {
                    val navBuilder = NavOptions.Builder()
                    val navOptions = navBuilder.setPopUpTo(R.id.nav_home, false).build()
                    val bundle = Bundle()
                    bundle.putString("restaurantGroupId", AppPreferences.group())
                    navController.navigate(it.itemId, bundle, navOptions)
                    drawerLayout.closeDrawer(Gravity.LEFT)
                    true
                }
            }

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun updateToken() {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (AppPreferences.isLogined) {
                    val map = HashMap<String, String>()
                    map["grant_type"] = "refresh_token"
                    map["username"] = ""
                    map["password"] = ""
                    map["refresh_token"] = AppPreferences.refreshToken.toString()

                    RetrofitClient.apiServiceCRM().refreshToken(map)
                        .enqueue(object : Callback<AuthModel> {
                            override fun onFailure(call: Call<AuthModel>, t: Throwable) {
                                println()
                            }

                            override fun onResponse(
                                call: Call<AuthModel>,
                                response: Response<AuthModel>
                            ) {
                                if (response.isSuccessful) {
                                    AppPreferences.accessToken = response.body()?.accessToken
                                    AppPreferences.refreshToken = response.body()?.refresh_token
                                } else {
                                    AppPreferences.clear()
//                                    Toast.makeText(this,"Вы не авторизованы нужно авторизоваться",Toa)
                                    findNavController(R.id.nav_host_fragment).navigate(R.id.nav_auth)
//                                    val intent = Intent(
//                                        this@MainActivity,
//                                        LoginActivity::class.java
//                                    ).putExtra("transition", true)
//                                    startActivity(intent)

                                }
                            }

                        })
                }

            }
        }, 1, 10000000)
    }
}
