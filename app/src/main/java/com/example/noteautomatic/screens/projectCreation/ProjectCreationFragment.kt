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
    private val pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
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

        binding.btnToRun.setOnClickListener {
            val direction =
                ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                    projectId =  args.projectId,
                    projectName = args.projectName
                )
            navigator().navigateTo(direction)
        }

        if (viewModel.fullProject == null) {
            navigator().toMenu()
        } else {
            viewModel.fullProject?.observe(viewLifecycleOwner) {
                binding.tvNameProject.text = it.name
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navigator().onToolbarVisibilityChanged(true)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        pickImages.launch(intent.toString())
    }
}