package com.osees.musterman

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.android.material.navigation.NavigationView
import com.osees.musterman.ui.profile.Profile
import com.osees.musterman.ui.settings.Settings

class NavigationMenu (private val navigationMenu: NavigationView, private val activity: Activity,
                      private val navController: NavController?, private val drawerLayout: DrawerLayout) {

    fun setFunctionForNavigationMenu() {
        navigationMenu.setNavigationItemSelectedListener(){
            when(it.itemId){
                R.id.nav_route -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is MainActivity -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_route
                            if (oldFragment != newFragment){
                                if (oldFragment != null && oldFragment != R.id.navigation_route) {
                                    navController?.popBackStack(oldFragment, true)
                                }
                                navController?.graph?.setStartDestination(R.id.navigation_route)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.putExtra("fragment", R.id.navigation_route)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            activity.startActivity(intent)
                            //activity.finish()
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_statistics -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is MainActivity -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_statistics
                            if (oldFragment != newFragment){
                                if (oldFragment != null && oldFragment != R.id.navigation_route) {
                                    navController?.popBackStack(oldFragment, true) }
                                navController?.graph?.setStartDestination(R.id.navigation_route)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.putExtra("fragment", R.id.navigation_statistics)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_results -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is MainActivity -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_results
                            if (oldFragment != newFragment){
                                if (oldFragment != null && oldFragment != R.id.navigation_route) {
                                    navController?.popBackStack(oldFragment, true) }
                                navController?.graph?.setStartDestination(R.id.navigation_route)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.putExtra("fragment", R.id.navigation_results)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_settings_profile -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is Settings -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_profile_settings
                            if (oldFragment != null && oldFragment != newFragment){
                                navController?.popBackStack(oldFragment, true)
                                navController?.graph?.setStartDestination(newFragment)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, Settings::class.java)
                            intent.putExtra("fragment", R.id.navigation_profile_settings)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_settings_route -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is Settings -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_route_settings
                            if (oldFragment != null && oldFragment != newFragment){
                                navController?.popBackStack(oldFragment, true)
                                navController?.graph?.setStartDestination(newFragment)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, Settings::class.java)
                            intent.putExtra("fragment", R.id.navigation_route_settings)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_settings_program -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity){
                        is Settings -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_program_settings
                            if (oldFragment != null && oldFragment != newFragment){
                                navController?.popBackStack(oldFragment, true)
                                navController?.graph?.setStartDestination(newFragment)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, Settings::class.java)
                            intent.putExtra("fragment", R.id.navigation_program_settings)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_info -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity) {
                        is Info -> {
                            Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            val intent = Intent(activity, Info::class.java)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity) {
                        is Profile -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_profile
                            if (oldFragment != null && oldFragment != newFragment){
                                navController?.popBackStack(oldFragment, true)
                                navController?.graph?.setStartDestination(newFragment)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, Profile::class.java)
                            intent.putExtra("fragment", R.id.navigation_profile)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_achievements -> {
                    drawerLayout.closeDrawer(navigationMenu)
                    when(activity) {
                        is Profile -> {
                            val oldFragment = navController?.currentDestination?.id
                            val newFragment = R.id.navigation_achievements
                            if (oldFragment != null && oldFragment != newFragment){
                                navController?.popBackStack(oldFragment, true)
                                navController?.graph?.setStartDestination(newFragment)
                                navController?.navigate(newFragment)
                                Log.d("my_tag", "navigate!")
                            }
                            else { Toast.makeText(activity, "Вы уже в данном разделе",Toast.LENGTH_LONG).show()}
                        }
                        else -> {
                            val intent = Intent(activity, Profile::class.java)
                            intent.putExtra("fragment", R.id.navigation_achievements)
                            activity.startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }
                else -> {return@setNavigationItemSelectedListener false}
            }

        }

    }
}