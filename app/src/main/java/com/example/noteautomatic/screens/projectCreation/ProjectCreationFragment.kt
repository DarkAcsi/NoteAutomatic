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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)
        navigator().onToolbarVisibilityChanged(true)
        with(binding) {
            setFieldName(false)
            viewModel.fullProject.observe(viewLifecycleOwner) { result ->
                renderSimpleResult(binding.root, result) {
                        if (it == null) {
                            navigator().toMenu()
                            navigator().toast("Couldn't find the project")
                        } else {
                            newProject = it
                            btnToRun.isEnabled = (it.listImage.isNullOrEmpty()) and (it.id != 0L)
                            if (it.id == 0L) {
                                tvNameProject.text = ""
                                edNameProject.hint = it.name
                                navigator().renameToolbar(it.name)
                            } else {
                                tvNameProject.text = it.name
                                navigator().renameToolbar(it.name)
                            }
                        }
                    }
            }
            viewModel.fullProject.observe(viewLifecycleOwner) {
            }
        }

        settingPage()

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
                    if (newProject.id  == 0L)
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
        }
    }

    private fun blockUI(){
        with(binding) {
            edNameProject.isEnabled = false
            btnRename.isEnabled = false
            btnAddImage.isEnabled = false
            btnSave.isEnabled = false
            btnToRun.isEnabled = false

            saveProgressBar.visibility = View.VISIBLE
        }
    }

    private fun unblockUI(){
        with(binding) {
            edNameProject.isEnabled = true
            btnRename.isEnabled = true
            btnAddImage.isEnabled = true
            btnSave.isEnabled = true
            btnToRun.isEnabled = true

            saveProgressBar.visibility = View.GONE
        }
    }

    private fun saveProjectChange() {
        var nameEd = binding.edNameProject.text.toString().trim()
        var nameTv = binding.tvNameProject.text.toString().trim()
        val speed = binding.edSpeed.text.toString().ifEmpty { binding.edSpeed.hint.toString() }
        viewModel.save(nameEd, nameTv, speed.toInt(), newProject)
        setFieldName(newProject.id == 0L)
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