package com.osees.musterman.ui.settings

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.osees.musterman.NavigationMenu
import com.osees.musterman.R
import com.osees.musterman.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)

        drawerLayout = binding.drawerLayout


        val bottomNavigationView = binding.navigationViewSettings
        val navigationView = binding.navView1



        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Сохранено!", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }

        val navController = findNavController(R.id.nav_host_fragment_content_settings)
        val navControllerGraph = navController.graph
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar


        val startFragment = intent.getIntExtra("fragment", R.id.navigation_profile_settings)
        navController.popBackStack(R.id.navigation_profile_settings, true)
        navControllerGraph.setStartDestination(startFragment)
        navController.navigate(startFragment)


        //appBarConfiguration = AppBarConfiguration(navControllerGraph)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_profile_settings, R.id.navigation_program_settings, R.id.navigation_route_settings
            ), drawerLayout
        )
        bottomNavigationView.setupWithNavController(navController)
        //setupActionBarWithNavController(navController, appBarConfiguration)
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