package com.example.noteautomatic.screens.projectRun

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.FragmentProjectRunBinding
import com.example.noteautomatic.screens.factory
import com.example.noteautomatic.screens.navigator

class ProjectRunFragment : Fragment(R.layout.fragment_project_run) {

    private lateinit var binding: FragmentProjectRunBinding

    private val viewModel: ProjectRunViewModel by viewModels { factory() }

    private val args: ProjectRunFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProjectRunBinding.bind(view)

        viewModel.projectRun.observe(viewLifecycleOwner, Observer {
            binding.tvNameProject.text = it.project.name
        })


        binding.btnToMenu.setOnClickListener {
            navigator().toMenu()
        }

        binding.btnToSetting.setOnClickListener {
            val direction =
                ProjectRunFragmentDirections.actionProjectRunFragmentToProjectCreationFragment(
                    projectId = args.projectId,
                    projectName = args.projectName ?: ""
                )
            navigator().navigateTo(direction)
        }
    }

}