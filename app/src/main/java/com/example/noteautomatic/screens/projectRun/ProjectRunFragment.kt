package com.example.noteautomatic.screens.projectRun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.noteautomatic.databinding.FragmentProjectRunBinding
import com.example.noteautomatic.screens.factory
import com.example.noteautomatic.screens.navigator
import com.example.noteautomatic.screens.projectsList.ProjectsListViewModel
import com.example.noteautomatic.screens.projectsList.projects.ProjectsAdapter

class ProjectRunFragment : Fragment() {

    private lateinit var binding: FragmentProjectRunBinding

    private val viewModel: ProjectRunViewModel by viewModels { factory() }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        viewModel.loadProject(requireArguments().getLong(ARG_PROJECT_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectRunBinding.inflate(inflater, container, false)

        viewModel.projectRun.observe(viewLifecycleOwner, Observer{
            binding.tvNameFullProject.text = it.project.name
        })

        binding.btnDeleteFullProject.setOnClickListener{
            viewModel.deleteProject()
            navigator().goBack()
        }

        return binding.root
    }

    companion object {
        private const val ARG_PROJECT_ID = "ARG_PROJECT_ID"

        fun newInstance(projectId: Long): ProjectRunFragment{
            val fragment = ProjectRunFragment()
            fragment.arguments = bundleOf(ARG_PROJECT_ID to projectId)
            return fragment
        }
    }
}