package com.osees.musterman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.osees.musterman.databinding.ActivityInfoBinding
import com.osees.musterman.databinding.ActivityProfileBinding

class Info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        //toggle.setDrawerIndicatorEnabled(true)
        toggle.syncState()
        NavigationMenu(navigationView, this, null, drawerLayout).setFunctionForNavigationMenu()


    }


    override fun onBackPressed() {
        if(drawerLayout.isOpen){
            drawerLayout.close()
            return
        }
        super.onBackPressed()
    }

}