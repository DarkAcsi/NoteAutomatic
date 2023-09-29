package com.example.noteautomatic.screens.projectCreation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectCreationBinding
import com.example.noteautomatic.navigator
import com.example.noteautomatic.viewModelCreator

class ProjectCreationFragment : Fragment(R.layout.fragment_project_creation) {

    private lateinit var binding: FragmentProjectCreationBinding

    private val viewModel by viewModelCreator { ProjectCreationViewModel(Repositories.projectsRepository) }

    private val args: ProjectCreationFragmentArgs by navArgs()
    private var newProject = true
        get() {return args.projectId == 0L}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)
        newProject = args.projectId == 0L
        navigator().onToolbarVisibilityChanged(true)
        with(binding) {
            setFieldName(false)
            viewModel.fullProject.observe(viewLifecycleOwner) {
                tvNameProject.text = it?.name
                btnToRun.isEnabled = !it?.listImage.isNullOrEmpty()
                navigator().renameToolbar(it?.name.toString())
            }
        }

        settingPage()

        with(binding) {

            btnRename.setOnClickListener { setFieldName(true) }

            btnAddImage.setOnClickListener { /*pickImage()*/ }

            btnCancelProject.setOnClickListener { navigator().toMenu() }

            btnSave.setOnClickListener { saveProjectChange() }

            btnToRun.setOnClickListener { runProject() }

        }
    }

    override fun onResume() {
        super.onResume()
        settingPage()
    }

    private fun settingPage() {
        navigator().onToolbarVisibilityChanged(true)
        with(binding) {
            setFieldName(false)
            viewModel.fullProject.observe(viewLifecycleOwner) {
                tvNameProject.text = it?.name
                btnToRun.isEnabled = !it?.listImage.isNullOrEmpty()
                navigator().renameToolbar(it?.name.toString())
            }
        }
    }

    private fun setFieldName(changed: Boolean) {
        with(binding) {
            when (changed) {
                true -> {
                    edNameProject.visibility = View.VISIBLE
                    tvNameProject.visibility = View.INVISIBLE
                    btnRename.visibility = View.INVISIBLE
                }
                false -> {
                    if (newProject)
                        setFieldName(true)
                    else {
                        edNameProject.visibility = View.INVISIBLE
                        tvNameProject.visibility = View.VISIBLE
                        btnRename.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun saveProjectChange() {
        val speed = binding.edSpeed.text.toString().ifEmpty { binding.edSpeed.hint.toString() }

        var name = binding.tvNameProject.text.toString()
        val projectName = viewModel.setNameProject(binding.edNameProject.text.toString(), name, newProject)
        if (projectName.isBlank()) {
            if (newProject)
                return
        }
        name = projectName
        if (viewModel.save(name, speed.toInt(), newProject) == null)
            navigator().toMenu()
        settingPage()
    }

    private fun runProject() {
        viewModel.fullProject.value?.let {
            val projectId = it.id
            val projectName = it.name
            val direction =
                ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                    projectId = projectId,
                    projectName = projectName
                )
            navigator().navigateTo(direction)
        }
    }
}