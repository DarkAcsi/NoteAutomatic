package com.example.noteautomatic

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteautomatic.databinding.ActivityMainBinding
import com.example.noteautomatic.screens.projectRun.ProjectRunFragment
import com.example.noteautomatic.screens.projectsList.projects.ProjectsAdapter
import com.example.noteautomatic.screens.projectsList.projects.ProjectsListener
import com.example.noteautomatic.screens.projectsList.projects.ProjectsService
import com.example.noteautomatic.screens.projectsList.ProjectsListFragment
import com.example.noteautomatic.screens.projectsList.projects.Project

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, ProjectsListFragment())
                .commit()
        }
    }

    override fun playProject(project: Project) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, ProjectRunFragment.newInstance(project.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}