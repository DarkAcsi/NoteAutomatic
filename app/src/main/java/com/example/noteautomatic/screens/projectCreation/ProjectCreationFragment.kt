package com.example.noteautomatic.screens.projectCreation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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

    private var imageUris: MutableList<Uri> = mutableListOf()
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris.clear()
            imageUris.addAll(uris)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)

        navigator().onToolbarVisibilityChanged(true)

        with(binding) {
            viewModel.fullProject.observe(viewLifecycleOwner) {
                tvNameProject.text = it.name
                btnToRun.isEnabled = !it.listImage.isNullOrEmpty()
                if (it.file.isNullOrBlank() and !it.listImage.isNullOrEmpty()) {
                    btnAddFile.visibility = View.GONE
                    btnAddImage.visibility = View.VISIBLE
                } else if (!it.file.isNullOrBlank()) {
                    btnAddFile.text = "Replace file"
                    btnAddFile.visibility = View.VISIBLE
                    btnAddImage.visibility = View.GONE
                } else {
                    btnAddFile.text = "Add file"
                    btnAddFile.visibility = View.VISIBLE
                    btnAddImage.visibility = View.VISIBLE
                }
            }
        }

        binding.btnToRun.setOnClickListener {
            val direction =
                ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                    projectId = args.projectId,
                    projectName = args.projectName
                )
            navigator().navigateTo(direction)
        }

        binding.edNameProject.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus and !viewModel.newProject) {
                val name = binding.edNameProject.text.toString()
                viewModel.setNameProject(name, binding.tvNameProject.text.toString())
                    .observe(viewLifecycleOwner) {
                        binding.tvNameProject.text = it
                    }
                setFieldName(false)
            }
        }

        binding.btnRename.setOnClickListener {
            setFieldName(true)
        }

        binding.btnCancelProject.setOnClickListener {
            navigator().toMenu()
        }

        binding.btnSave.setOnClickListener {
            var name = binding.tvNameProject.text.toString()
            val speed = binding.edSpeed.text.toString().toInt()
            if (viewModel.newProject)
                viewModel.setNameProject(binding.edNameProject.text.toString(), "")
                    .observe(viewLifecycleOwner) {
                        if (it.isEmpty()) binding.edNameProject.hint = "Input name"
                        else {
                            name = it
                            viewModel.newProject = false
                            setFieldName(false)
                        }
                    }
            viewModel.save(name, speed)
        }

        binding.btnAddImage.setOnClickListener {
            pickImage()
        }
    }

    override fun onResume() {
        super.onResume()
        navigator().onToolbarVisibilityChanged(true)
    }

    private fun setFieldName(changed: Boolean) {
        with(binding) {
            when (changed) {
                true -> {
                    edNameProject.visibility = View.VISIBLE
                    tvNameProject.visibility = View.INVISIBLE
                    btnRename.visibility = View.INVISIBLE
                    if (!viewModel.newProject)
                        edNameProject.requestFocus()
                }

                false -> {
                    if (viewModel.newProject)
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

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        pickImages.launch(intent.toString())
    }
}