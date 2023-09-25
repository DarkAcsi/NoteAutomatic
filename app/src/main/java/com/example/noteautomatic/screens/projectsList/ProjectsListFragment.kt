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
    private var selected: Boolean = false
    private var selectedAll: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectListBinding.bind(view)

        navigator().onToolbarVisibilityChanged(false)
        binding.toolbar.toolbarSelected.visibility = View.INVISIBLE

        binding.btnAddProject.setOnClickListener {
            if (!selected) {
                val direction =
                    ProjectsListFragmentDirections.actionProjectsListFragmentToProjectCreationFragment(
                        projectId = 0,
                        projectName = ""
                    )
                navigator().navigateTo(direction)
            } else {
                stateToolbarButton(false)
                selected = false
                viewModel.deleteProjects()
            }
        }

        binding.toolbar.btnCancel.setOnClickListener {
            stateToolbarButton(false)
            selected = false
            viewModel.selectAllProjects(null)
        }

        binding.toolbar.cbSelectedAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedAll = true
                viewModel.selectAllProjects(true)
            }
        }

        adapter = ProjectsAdapter(object : ProjectActionListener {

            override fun onProjectSetting(project: Project) {
                val direction =
                    ProjectsListFragmentDirections.actionProjectsListFragmentToProjectCreationFragment(
                        projectId = project.id,
                        projectName = project.name
                    )
                navigator().navigateTo(direction)
            }

            override fun onProjectsSelect(project: Project, select: Boolean) {
                selected = select
                stateToolbarButton(selected)
                viewModel.selectProjects(project, selected)
            }

            override fun onProjectSelectMore(project: Project): Boolean {
                selected = viewModel.selectMoreProject(project)
                stateToolbarButton(selected)
                return selected
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
        layoutManager.reverseLayout = true;
        layoutManager.stackFromEnd = true;
        binding.rvProjectList.layoutManager = layoutManager
        binding.rvProjectList.adapter = adapter

        val itemAnimator = binding.rvProjectList.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    override fun onResume() {
        super.onResume()
        navigator().onToolbarVisibilityChanged(false)
        binding.toolbar.toolbarSelected.visibility = View.GONE
    }

    private fun stateToolbarButton(select: Boolean) {
        with(binding) {
            if (select) {
                btnAddProject.text = "Delete"
                toolbar.toolbarSelected.visibility = View.VISIBLE
            } else {
                btnAddProject.text = "Add"
                toolbar.toolbarSelected.visibility = View.GONE
            }
        }
    }
}
