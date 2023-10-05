package com.example.noteautomatic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.example.noteautomatic.databinding.ActivityMainBinding
import com.example.noteautomatic.screens.projectsList.BackPressed

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Repositories.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

    }

    override fun onSupportNavigateUp(): Boolean {
        when (navController.currentDestination?.id) {
            R.id.projectCreationFragment -> {
                toMenu()
                return true
            }
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateUp() {
        onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val currentFragment = navHost.childFragmentManager.fragments[0]

        if (currentFragment is BackPressed) currentFragment.onBackPressed()
        else onSupportNavigateUp()
    }

    override fun navigateTo(direction: NavDirections) {
        navController.navigate(direction)
    }

    override fun navigateTo(destination: Int, args: Bundle?) {
        navController.navigate(destination, args)
    }

    override fun toMenu() {
        navController.popBackStack(R.id.projectsListFragment, true)
        navController.navigate(R.id.projectsListFragment)
    }

    override fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}