package com.example.fitpho

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.fitpho.util.KeepStateFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //NavController
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var menu_nav: BottomNavigationView = findViewById(R.id.menu_nav)
        var list: ArrayList<Int> = arrayListOf(R.id.homeFragment, R.id.guideFragment, R.id.settingFragment ,  R.id.calenderFragment, R.id.aiMovementFragment)

        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment

        navController = hostFragment.navController

        val navigator = KeepStateFragment(this, hostFragment.childFragmentManager, R.id.nav_controller)
        navController.navigatorProvider.addNavigator(navigator)

        NavigationUI.setupWithNavController(menu_nav, navController)


        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(list.contains(destination.id)){
                menu_nav.visibility = View.VISIBLE
            }else{
                menu_nav.visibility = View.GONE
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController
        return navController.navigateUp()
    }

}

