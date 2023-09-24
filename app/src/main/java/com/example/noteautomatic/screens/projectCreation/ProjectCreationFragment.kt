package com.example.noteautomatic.screens.projectCreation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.FragmentProjectCreationBinding
import com.example.noteautomatic.screens.factory
import com.example.noteautomatic.screens.navigator

class ProjectCreationFragment : Fragment(R.layout.fragment_project_creation) {

    private lateinit var binding: FragmentProjectCreationBinding

    private val viewModel: ProjectCreationViewModel by viewModels { factory() }

    private val args: ProjectCreationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)

        binding.btnToMenu.setOnClickListener {
            navigator().toMenu()
        }

        binding.btnToRun.setOnClickListener {
            val direction =
                ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                    projectId =  args.projectId,
                    projectName = args.projectName ?: ""
                )
            navigator().navigateTo(direction)
        }

    }
}