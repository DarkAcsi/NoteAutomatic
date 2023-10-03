package com.example.noteautomatic.screens.projectRun

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectRunBinding
import com.example.noteautomatic.foundation.base.BaseFragment
import com.example.noteautomatic.navigator
import com.example.noteautomatic.screens.renderSimpleResult
import com.example.noteautomatic.viewModelCreator

class ProjectRunFragment : BaseFragment(R.layout.fragment_project_run) {

    private lateinit var binding: FragmentProjectRunBinding
    private lateinit var adapter: ImagesRunAdapter

    override val viewModel by viewModelCreator { ProjectRunViewModel(Repositories.imagesRepository) }

    private val args: ProjectRunFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectRunBinding.bind(view)
        viewModel.loadImages(args.projectId)
        // binding.toolbar.tvName.text = args.projectName

        navigator().onToolbarVisibilityChanged(false)

        createRecyclerView()

        with(binding){
            viewModel.images.observe(viewLifecycleOwner) {result ->
                renderSimpleResult(binding.root, result) {
                    adapter.images = it
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        navigator().onToolbarVisibilityChanged(false)
    }

    private fun createRecyclerView() {
        adapter = ImagesRunAdapter(object : ImagesRunListener{
            override fun setPause() {
                navigator().onToolbarVisibilityChanged(true)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRunProject.layoutManager = layoutManager
        binding.rvRunProject.adapter = adapter
    }

}