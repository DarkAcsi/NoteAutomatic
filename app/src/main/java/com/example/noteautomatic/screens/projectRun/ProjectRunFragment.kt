package com.example.noteautomatic.screens.projectRun

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectRunBinding
import com.example.noteautomatic.navigator
import com.example.noteautomatic.viewModelCreator

class ProjectRunFragment : Fragment(R.layout.fragment_project_run) {

    private lateinit var binding: FragmentProjectRunBinding

    private val viewModel by viewModelCreator { ProjectRunViewModel(Repositories.projectsRepository) }

    private val args: ProjectRunFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectRunBinding.bind(view)

        setPlay()

        binding.root.setOnClickListener { setPause() }

        binding.toolbar.ibHome.setOnClickListener { navigator().toMenu() }

        binding.toolbar.ibBack.setOnClickListener { navigator().navigateUp() }

        binding.ibRestart.setOnClickListener {}

        binding.ibPlay.setOnClickListener { setPlay() }

        binding.ibUp.setOnClickListener {}

        binding.ibDown.setOnClickListener {}

    }

    override fun onPause() {
        super.onPause()
        setPause()

    }

    override fun onResume() {
        super.onResume()
        setPlay()
    }

    private fun setPause() {
        binding.toolbar.toolbarRun.visibility = View.VISIBLE
        binding.layoutPause.visibility = View.VISIBLE
    }

    private fun setPlay() {
        binding.toolbar.toolbarRun.visibility = View.GONE
        binding.layoutPause.visibility = View.GONE
    }

}