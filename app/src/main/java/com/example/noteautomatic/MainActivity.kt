package com.example.noteautomatic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.noteautomatic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean{
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun navigateTo(direction: NavDirections) {
        navController.navigate(direction)
    }

    override fun navigateTo(destination: Int, args: Bundle?) {
        navController.navigate(destination, args)
    }

    override fun toMenu() {
        navController.popBackStack(R.id.projectsListFragment, false)
    }

    override fun goBack() {
        onSupportNavigateUp()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}