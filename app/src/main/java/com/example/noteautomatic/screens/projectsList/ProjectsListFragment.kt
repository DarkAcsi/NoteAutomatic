package com.example.noteautomatic.screens.projectsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.databinding.FragmentProjectListBinding
import com.example.noteautomatic.screens.projectsList.projects.Project
import com.example.noteautomatic.screens.projectsList.projects.ProjectActionListener
import com.example.noteautomatic.screens.projectsList.projects.ProjectsAdapter
import com.example.noteautomatic.screens.factory

class ProjectsListFragment : Fragment() {

    private lateinit var binding: FragmentProjectListBinding
    private lateinit var adapter: ProjectsAdapter

    private val viewModel: ProjectsListViewModel by viewModels { factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectListBinding.inflate(inflater, container, false)
        adapter = ProjectsAdapter(object : ProjectActionListener {

            override fun onProjectSetting(project: Project) {
                viewModel.settingProject(project)
            }

            override fun onProjectDelete(project: Project) {
                viewModel.deleteProject(project)
            }

            override fun onProjectMoveUp(project: Project) {
                viewModel.moveUpProject(project)
            }

            override fun onProjectSelect(project: Project) {
                viewModel.selectProject(project)
            }

            override fun onProjectDetails(project: Project) {
                // todo onProjectDetails
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

        return binding.root
    }

}