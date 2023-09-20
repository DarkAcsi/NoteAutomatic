package com.example.noteautomatic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteautomatic.databinding.ActivityMainBinding
import com.example.noteautomatic.screens.projectsList.projects.ProjectsAdapter
import com.example.noteautomatic.screens.projectsList.projects.ProjectsListener
import com.example.noteautomatic.screens.projectsList.projects.ProjectsService
import com.example.noteautomatic.screens.projectsList.ProjectsListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProjectsAdapter

    private val projectsService: ProjectsService
        get() = (applicationContext as App).projectsService

    private val projectsListener: ProjectsListener = {
        adapter.projects = it
    }

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
}