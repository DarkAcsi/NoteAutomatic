package com.example.noteautomatic.screens.projectRun

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectRunBinding
import com.example.noteautomatic.foundation.base.BaseFragment
import com.example.noteautomatic.navigator
import com.example.noteautomatic.screens.onTryAgain
import com.example.noteautomatic.screens.renderSimpleResult
import com.example.noteautomatic.viewModelCreator

class ProjectRunFragment : BaseFragment(R.layout.fragment_project_run) {

    private lateinit var binding: FragmentProjectRunBinding
    private lateinit var adapter: ImagesRunAdapter
    private var midScreenSpeed = 0f
    private var midScreenTime = 0L

    override val viewModel by viewModelCreator { ProjectRunViewModel(Repositories.imagesRepository) }

    private val args: ProjectRunFragmentArgs by navArgs()
    private var speed: Int = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectRunBinding.bind(view)
        viewModel.loadImages(args.projectId)
        binding.toolbar.tvName.text = args.projectName

        setPause()
        createRecyclerView()
        movePosition(0)

        with(binding) {
            viewModel.imagesFiles.observe(viewLifecycleOwner) { result ->
                renderSimpleResult(root, result) {
                    val previousPosition =
                        (rvRunProject.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    adapter.images = it
                    rvRunProject.scrollToPosition(previousPosition)
                }
            }

            toolbar.ibHome.setOnClickListener { navigator().toMenu() }
            createRecyclerView()

            toolbar.ibBack.setOnClickListener { navigator().navigateUp() }

            ibRestart.setOnClickListener { movePosition(0) }

            ibPlay.setOnClickListener {
                setPlay()
                runProject()
            }

            ibUp.setOnClickListener { movePosition(-1) }

            ibDown.setOnClickListener { movePosition(1) }

            onTryAgain(root) { viewModel.tryAgain() }
        }

    }

    override fun onPause() {
        super.onPause()
        setPause()
    }

    override fun onResume() {
        super.onResume()
        setPause()
    }

    private fun setPause() {
        binding.toolbar.toolbarRun.visibility = View.VISIBLE
        binding.layoutPause.visibility = View.VISIBLE
    }

    private fun setPlay() {
        binding.toolbar.toolbarRun.visibility = View.GONE
        binding.layoutPause.visibility = View.GONE
    }

    private fun createRecyclerView() {
        adapter = ImagesRunAdapter(object : ImagesRunListener {
            override fun setPauseScroll() {
                setPause()
            }
        })
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRunProject.layoutManager = layoutManager
        binding.rvRunProject.adapter = adapter

        speed = args.projectSpeed
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        midScreenSpeed =
            if (args.projectSpeed == 0) Float.POSITIVE_INFINITY
            else 1000000f / (displayMetrics.densityDpi * args.projectSpeed)
        midScreenTime = (screenHeight / 2f * midScreenSpeed).toLong()
    }

    private fun runProject() {
        val layoutManager = binding.rvRunProject.layoutManager

        val smoothScroller = SmoothScroller()

        binding.rvRunProject.postDelayed({
            smoothScroller.targetPosition = adapter.itemCount - 1
            layoutManager?.startSmoothScroll(smoothScroller)
        }, midScreenTime)
    }

    private fun movePosition(move: Int) {
        val layoutManager = binding.rvRunProject.layoutManager as LinearLayoutManager
        val currentFirstPosition = layoutManager.findFirstVisibleItemPosition()
        val latestPosition = adapter.itemCount - 1


        val position = when (move) {
            -1 -> if (currentFirstPosition == 0) 0
            else currentFirstPosition - 1

            1 -> if (currentFirstPosition == latestPosition) currentFirstPosition
            else currentFirstPosition + 1

            else -> 0
        }

        val smoothScroller = object : LinearSmoothScroller(binding.rvRunProject.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        smoothScroller.targetPosition = position
        layoutManager.startSmoothScroll(smoothScroller)

    }

    inner class SmoothScroller :
        LinearSmoothScroller(requireContext()) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            if (speed != 0) {
                return 1000000f / (displayMetrics.densityDpi * speed)
            }
            midScreenSpeed = 0f
            return Float.POSITIVE_INFINITY
        }
    }

}