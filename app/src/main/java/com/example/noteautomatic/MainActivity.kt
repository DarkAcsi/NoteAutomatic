package com.example.noteautomatic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import com.example.noteautomatic.databinding.ActivityMainBinding
import com.example.noteautomatic.foundation.Navigator

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Repositories.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.projectsListFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.toolbar.title = ""
                }

                R.id.projectCreationFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    arguments?.let {
                        val projectName = it.getString("projectName") ?: ""
                        binding.toolbar.title = projectName
                    }
                }

                R.id.projectRunFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    arguments?.let {
                        val projectName = it.getString("projectName") ?: ""
                        binding.toolbar.title = projectName
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.projectCreationFragment) {
            toMenu()
            return true
        }
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java", ReplaceWith("onSupportNavigateUp()"))
    override fun onBackPressed() {
        onSupportNavigateUp()
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

    override fun onToolbarVisibilityChanged(visible: Boolean) {
        if (visible) {
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun renameToolbar(name: String) {
        binding.toolbar.title = name
    }

    override fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}