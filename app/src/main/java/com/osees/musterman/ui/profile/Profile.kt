package com.osees.musterman.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.osees.musterman.NavigationMenu
import com.osees.musterman.R
import com.osees.musterman.databinding.ActivityProfileBinding
import com.osees.musterman.databinding.ActivitySettingsBinding

class Profile : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityProfileBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_profile)
        val navControllerGraph = navController.graph


        val startFragment = intent.getIntExtra("fragment", R.id.navigation_profile)
        navController.popBackStack(R.id.navigation_profile, true)
        navControllerGraph.setStartDestination(startFragment)
        navController.navigate(startFragment)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile, R.id.navigation_achievements), drawerLayout
        )
        NavigationUI.setupWithNavController(toolbar, navController,appBarConfiguration)

        NavigationMenu(navigationView, this, navController, drawerLayout).setFunctionForNavigationMenu()
    }

    override fun onBackPressed() {
        if(drawerLayout.isOpen){
            drawerLayout.close()
            return
        }
        super.onBackPressed()
    }
}