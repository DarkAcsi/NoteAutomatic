package com.example.noteautomatic.screens.projectsList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.FragmentProjectListBinding
import com.example.noteautomatic.screens.factory
import com.example.noteautomatic.screens.navigator

class ProjectsListFragment : Fragment(R.layout.fragment_project_list) {

    private lateinit var binding: FragmentProjectListBinding
    private lateinit var adapter: ProjectsAdapter

    private val viewModel: ProjectsListViewModel by viewModels { factory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectListBinding.bind(view)
        adapter = ProjectsAdapter(object : ProjectActionListener {

            override fun onProjectSetting(project: Project) {
                val direction =
                    ProjectsListFragmentDirections.actionProjectsListFragmentToProjectCreationFragment(
                        projectId = project.id,
                        projectName = project.name
                    )
                navigator().navigateTo(direction)
            }

            override fun onProjectDelete(project: Project) {
                viewModel.deleteProject(project)
            }

            override fun onProjectSelect(project: Project) {
                viewModel.selectProject(project)
            }

            override fun onProjectPlay(project: Project) {
                val direction =
                    ProjectsListFragmentDirections.actionProjectsListFragmentToProjectRunFragment(
                        projectId = project.id,
                        projectName = project.name
                    )
                navigator().navigateTo(direction)
            }
        })

        viewModel.projects.observe(viewLifecycleOwner) {
            adapter.projects = it
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvProjectList.layoutManager = layoutManager
        binding.rvProjectList.adapter = adapter

        val itemAnimator = binding.rvProjectList.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

}