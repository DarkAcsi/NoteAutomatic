package com.example.noteautomatic.screens.projectCreation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectCreationBinding
import com.example.noteautomatic.foundation.base.BaseFragment
import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.navigator
import com.example.noteautomatic.foundation.viewModelCreator
import com.example.noteautomatic.screens.renderSimpleResult

class ProjectCreationFragment : BaseFragment(R.layout.fragment_project_creation) {

    private lateinit var binding: FragmentProjectCreationBinding

    override val viewModel by viewModelCreator { ProjectCreationViewModel(Repositories.projectsRepository) }

    private val args: ProjectCreationFragmentArgs by navArgs()
    private var newProject = FullProject(0, "")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)
        viewModel.loadProject(args.projectId)
        with(binding) {
            viewModel.viewState.observe(viewLifecycleOwner) { result ->
                renderSimpleResult(binding.root, result) {
                    blockUI(!it.isSaving)
                    newProject = it.fullProject
                    settingPage()
                    if (newProject.id == 0L) {
                        tvNameProject.text = ""
                        edNameProject.hint = newProject.name
                        navigator().renameToolbar(newProject.name)
                    } else {
                        tvNameProject.text = newProject.name
                        navigator().renameToolbar(newProject.name)
                    }
                }
            }
        }

        with(binding) {

            edNameProject.setOnFocusChangeListener { _, hasFocus -> changeFocus(hasFocus) }

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
        setFieldName(false)
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
                    if (newProject.id == 0L)
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

    private fun changeFocus(hasFocus: Boolean) {
        if ((!hasFocus) and (newProject.id != 0L)) {
            setFieldName(false)
            binding.tvNameProject.text =
                binding.edNameProject.text.ifBlank { binding.tvNameProject.text }
        }
    }

    private fun blockUI(isEnabled: Boolean) {
        with(binding) {
            edNameProject.isEnabled = isEnabled
            btnRename.isEnabled = isEnabled
            btnAddImage.isEnabled = isEnabled
            btnAddFile.isEnabled = isEnabled
            btnSave.isEnabled = isEnabled
            sbSpeed.isEnabled = isEnabled
            edSpeed.isEnabled = isEnabled
            btnToRun.isEnabled =
                (newProject.listImage.isNullOrEmpty()) and (newProject.id != 0L)

            saveProgressBar.visibility = if (!isEnabled) View.VISIBLE else View.GONE
        }
    }

    private fun saveProjectChange() {
        val nameEd = binding.edNameProject.text.toString().trim()
        val speed = binding.edSpeed.text.toString().ifEmpty { binding.edSpeed.hint.toString() }
        viewModel.save(nameEd, speed.toInt(), newProject)
    }

    private fun runProject() {
        val direction =
            ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                projectId = newProject.id,
                projectName = newProject.name
            )
        navigator().navigateTo(direction)
    }
}