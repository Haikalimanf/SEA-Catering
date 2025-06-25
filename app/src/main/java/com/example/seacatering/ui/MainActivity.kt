package com.example.seacatering.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.seacatering.R
import com.example.seacatering.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_menu, R.id.navigation_subscription
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_contactUs -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.navigation_contactUs)
                true
            }
            R.id.navigation_profile -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.navigation_profile)
                true
            }
            R.id.navigation_dashboard -> {
                findNavController(R.id.nav_host_fragment_activity_main)
                    .navigate(R.id.navigation_dashboard)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}