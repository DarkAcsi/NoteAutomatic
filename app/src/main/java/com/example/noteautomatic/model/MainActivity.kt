package com.example.noteautomatic.model

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.App
import com.example.noteautomatic.databinding.ActivityMainBinding

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

        adapter = ProjectsAdapter(object : ProjectActionListener {

            override fun onProjectRename(project: Project) {
//                TODO("Not yet implemented")
            }

            override fun onProjectMoveUp(project: Project) {
                projectsService.moveUpProject(project)
            }

            override fun onProjectDelete(project: Project) {
                projectsService.deleteProject(project)
            }

            override fun onProjectDetail(project: Project) {
                Toast.makeText(this@MainActivity, "Project: ${project.name}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        binding.rvProjectList.layoutManager = layoutManager
        binding.rvProjectList.adapter = adapter

        val itemAnimator = binding.rvProjectList.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        projectsService.addListener(projectsListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        projectsService.removeListener(projectsListener)
    }

}