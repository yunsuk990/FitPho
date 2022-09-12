package com.example.fitpho

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var menu_nav: BottomNavigationView = findViewById(R.id.menu_nav)
        var actionBar: ActionBar? = supportActionBar
        var list: Array<Int> = arrayOf(R.id.splashFragment, R.id.loginFragment, R.id.registerFragment)


        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController,
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.homeFragment, R.id.guideFragment
            , R.id.calenderFragment, R.id.settingFragment).build()
        )
        NavigationUI.setupWithNavController(menu_nav, navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if((destination.id == R.id.splashFragment) || (destination.id == R.id.loginFragment) || (destination.id == R.id.registerFragment)){
                menu_nav.visibility = View.GONE
                if((R.id.splashFragment == destination.id)){
                    actionBar?.hide()
                }else{
                    actionBar?.show()
                }
            }else{
                actionBar?.show()
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
