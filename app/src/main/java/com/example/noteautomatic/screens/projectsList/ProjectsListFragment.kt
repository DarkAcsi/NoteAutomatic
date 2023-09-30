package com.example.noteautomatic.screens.projectsList

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectListBinding
import com.example.noteautomatic.foundation.base.BaseFragment
import com.example.noteautomatic.foundation.classes.Project
import com.example.noteautomatic.foundation.navigator
import com.example.noteautomatic.foundation.viewModelCreator
import com.example.noteautomatic.screens.onTryAgain
import com.example.noteautomatic.screens.renderSimpleResult

class ProjectsListFragment : BaseFragment(R.layout.fragment_project_list) {

    private lateinit var binding: FragmentProjectListBinding
    private lateinit var adapter: ProjectsAdapter

    override val viewModel by viewModelCreator { ProjectsListViewModel(Repositories.projectsRepository) }
    private var selected: Boolean = false
    private var selectedAll: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectListBinding.bind(view)

        settingPage()
        createRecyclerView()

        with(binding) {

            viewModel.projects.observe(viewLifecycleOwner) { result ->
                renderSimpleResult(
                    root = root,
                    result = result,
                    onSuccess = {
                        if (it.isEmpty()) ivEmptyList.visibility = View.VISIBLE
                        else rvProjectList.visibility = View.VISIBLE
                        adapter.projects = it
                    }
                )
            }
            btnAddProject.visibility = View.VISIBLE
            onTryAgain(root) {
                viewModel.tryAgain()
            }

            btnAddProject.setOnClickListener { addNewProject() }

            toolbar.btnCancel.setOnClickListener {
                stateToolbarButton(false)
                selected = false
                viewModel.selectAllProjects(null)
            }

            toolbar.cbSelectedAll.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedAll = true
                    viewModel.selectAllProjects(true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        settingPage()
    }

    private fun settingPage() {
        navigator().onToolbarVisibilityChanged(false)
        binding.toolbar.toolbarSelected.visibility = View.INVISIBLE
    }

    private fun addNewProject() {
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

    private fun createRecyclerView() {
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
                if (project.play) {
                    val direction =
                        ProjectsListFragmentDirections.actionProjectsListFragmentToProjectRunFragment(
                            projectId = project.id,
                            projectName = project.name
                        )
                    navigator().navigateTo(direction)
                }
            }
        })

        with(binding) {

            val layoutManager = LinearLayoutManager(requireContext())
            layoutManager.reverseLayout = true;
            layoutManager.stackFromEnd = true;
            rvProjectList.layoutManager = layoutManager
            rvProjectList.adapter = adapter

            val itemAnimator = rvProjectList.itemAnimator
            if (itemAnimator is DefaultItemAnimator) {
                itemAnimator.supportsChangeAnimations = false
            }
        }
    }
}
