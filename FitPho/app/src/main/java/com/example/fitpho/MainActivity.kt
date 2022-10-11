package com.example.fitpho

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fitpho.Register.RegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //Database 가져오기
    //val Database by lazy { AppDatabase.getDatabase(this).guideDao() }

    //NavController
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var menu_nav: BottomNavigationView = findViewById(R.id.menu_nav)
        var actionBar: ActionBar? = supportActionBar
        var list: ArrayList<Int> = arrayListOf(R.id.splashFragment, R.id.loginFragment, R.id.registerFragment, R.id.welcomeFragment, R.id.findPasswordFragment , R.id.newPasswordFragment
            ,R.id.emailAuthorization, R.id.startFragment, R.id.findPasswordAuthFragment, R.id.guideDetailFragment, R.id.detailFragment)

        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController
//        NavigationUI.setupActionBarWithNavController(this, navController,
//            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.homeFragment, R.id.guideFragment
//            , R.id.calenderFragment, R.id.settingFragment).build()
//        )
        NavigationUI.setupWithNavController(menu_nav, navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            //actionBar?.show()
            if(list.contains(destination.id)){
                menu_nav.visibility = View.GONE
                //actionBar?.hide()
            }else{
                menu_nav.visibility = View.VISIBLE
                //actionBar?.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var hostFragment = supportFragmentManager.findFragmentById(R.id.nav_controller) as NavHostFragment
        navController = hostFragment.navController
        return navController.navigateUp()
    }

}

