package com.osees.musterman

import android.app.AlertDialog
import com.osees.musterman.ui.settings.Settings
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.osees.musterman.databinding.ActivityMainBinding
import com.osees.musterman.ui.route.RouteCreatorObjects
import com.osees.musterman.ui.route.RouteFragment
import com.osees.musterman.ui.route.RouteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainSharedPreferences = MainSharedPreferences(this)
        mainSharedPreferences.deleteSharedPreferences(deleteAllSharedPreferences = true)

        if (!mainSharedPreferences.isCreated("MainCharacteristics")){
            mainSharedPreferences.createDefaultMainPref()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //mainSharedPreferences.deleteSharedPreferences("MainCharacteristics")


        val navView: BottomNavigationView = binding.navView
        val navView1: NavigationView = binding.navView1

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val navControllerGraph = navController.graph
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_route, R.id.navigation_results, R.id.navigation_statistics
            ), drawerLayout
        )
        NavigationUI.setupWithNavController(toolbar, navController,appBarConfiguration)

        //Log.d("my_tag", "intent action:" + intent.action)
        if (intent.action == null){
            val startFragment = intent.getIntExtra("fragment", R.id.navigation_route)
            navController.popBackStack(R.id.navigation_route, true)
            navControllerGraph.setStartDestination(R.id.navigation_route)
            navController.navigate(startFragment)
        }


        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.route_menu_clear -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Очистить маршрут")
                    builder.setMessage("Вы уверены?")

                    builder.setPositiveButton("Да"){ _, _ ->
                        mainSharedPreferences.deleteSharedPreferences(deleteAllConsumption = true, deleteAllRoute = true)
                        mainSharedPreferences.clearMainSharedPreferences()


                        val routeViewModel = ViewModelProvider(this)[RouteViewModel::class.java]

                        val nowFragment: Fragment = this.supportFragmentManager.fragments[0]

                        if(nowFragment.isVisible){
                            val root = nowFragment.view?.rootView?.findViewById<ViewGroup>(R.id.linear_layout_scroll)
                            this.let {
                                if (root != null) {
                                    RouteCreatorObjects(root, this, routeViewModel).redrawRouteObjects()
                                }
                            }
                        }

                        routeViewModel.setDefaultRoute(true)
                        routeViewModel.setDefaultConsumption(true)
                        Toast.makeText(this, "Сделано!", Toast.LENGTH_SHORT).show()
                    }

                    builder.setNeutralButton("Отмена"){dialogInterface , which ->
                    }
                    //builder.setNegativeButton("No"){dialogInterface, which ->
                    //    Toast.makeText(activity,"clicked No",Toast.LENGTH_LONG).show()
                    //}

                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                    return@setOnMenuItemClickListener true
                }
                R.id.route_menu_settings -> {
                    val intent = Intent(this, Settings::class.java)
                    intent.putExtra("fragment", R.id.navigation_route_settings)
                    startActivity(intent)
                    return@setOnMenuItemClickListener true
                }
                else -> {return@setOnMenuItemClickListener false}
            }
        }

        NavigationMenu(navView1, this, navController, drawerLayout).setFunctionForNavigationMenu()

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(drawerLayout.isOpen){
            drawerLayout.close()
            return
        }
        super.onBackPressed()
    }
}