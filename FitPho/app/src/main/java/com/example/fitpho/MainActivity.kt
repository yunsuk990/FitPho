package com.example.fitpho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.collection.arrayMapOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fitpho.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var menu_nav: BottomNavigationView = findViewById(R.id.menu_nav)

        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController


        NavigationUI.setupActionBarWithNavController(this, navController,
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.homeFragment, R.id.guideFragment
            , R.id.calenderFragment, R.id.settingFragment).build()
        )
        NavigationUI.setupWithNavController(menu_nav, navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if( (R.id.splashFragment == destination.id) ||  (R.id.loginFragment == destination.id)){
                menu_nav.visibility = View.GONE
            }else{
                menu_nav.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController
        return navController.navigateUp()
    }
}
