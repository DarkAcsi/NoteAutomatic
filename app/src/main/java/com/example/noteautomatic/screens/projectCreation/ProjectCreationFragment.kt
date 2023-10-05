package com.example.noteautomatic.screens.projectCreation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.SeekBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectCreationBinding
import com.example.noteautomatic.foundation.base.BaseFragment
import com.example.noteautomatic.foundation.database.entities.Project
import com.example.noteautomatic.navigator
import com.example.noteautomatic.screens.onTryAgain
import com.example.noteautomatic.screens.renderSimpleResult
import com.example.noteautomatic.viewModelCreator

class ProjectCreationFragment : BaseFragment(R.layout.fragment_project_creation) {

    private lateinit var binding: FragmentProjectCreationBinding
    private lateinit var adapter: ImagesAdapter
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var pdfPickerLauncher: ActivityResultLauncher<Intent>


    override val viewModel by viewModelCreator {
        ProjectCreationViewModel(
            Repositories.projectsRepository,
            Repositories.imagesRepository
        )
    }

    private val args: ProjectCreationFragmentArgs by navArgs()
    private var newProject = Project(0, "")

    private val requestPermission = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)
        viewModel.loadProject(args.projectId)
        createRecyclerView()

        with(binding) {
            viewModel.viewState.observe(viewLifecycleOwner) { result ->
                renderSimpleResult(binding.root, result) {
                    blockUI(!it.isSaving)
                    newProject = it.project
                    adapter.images = it.listImage
                    settingPage()
                    if (newProject.id == 0L) {
                        tvNameProject.text = ""
                        edNameProject.hint = newProject.name
                        toolbar.tvName.text = newProject.name
                    } else {
                        tvNameProject.text = newProject.name
                        sbSpeed.progress = newProject.speed
                        edSpeed.setText(newProject.speed.toString())
                        toolbar.tvName.text = newProject.name
                        btnToRun.isEnabled =
                            (!it.isSaving) and (newProject.play) and (newProject.id != 0L)
                    }
                }
            }

            onTryAgain(root) {
                viewModel.tryAgain()
            }
        }

        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvImages)

        with(binding) {

            edNameProject.setOnFocusChangeListener { _, hasFocus -> changeFocus(hasFocus) }

            btnRename.setOnClickListener { setFieldName(true) }

            btnAddImage.setOnClickListener { pickImages() }

            btnAddFile.setOnClickListener { pickPdf() }

            sbSpeed.setOnSeekBarChangeListener(Seekbar())

            edSpeed.setOnFocusChangeListener { _, hasFocus -> changeSpeed(hasFocus) }

            btnCancelProject.setOnClickListener { navigator().toMenu() }

            toolbar.ibBack.setOnClickListener { navigator().navigateUp() }

            toolbar.ibDelete.setOnClickListener { deleteProject() }

            btnSave.setOnClickListener { saveProjectChange() }

            btnToRun.setOnClickListener { runProject() }

        }

        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.createImagePicker(result, newProject)
            }

        pdfPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.createPdfPicker(result, newProject, requireContext())
            }
    }

    private fun createRecyclerView() {
        viewModel.loadProject(newProject.id)
        adapter = ImagesAdapter(viewModel)
        with(binding) {
            val layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvImages.layoutManager = layoutManager
            rvImages.adapter = adapter
            rvImages.itemAnimator = DefaultItemAnimator().apply {
                moveDuration = 600
            }
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
                isEnabled and (newProject.play) and (newProject.id != 0L)

            saveProgressBar.visibility = if (!isEnabled) View.VISIBLE else View.GONE
        }
    }

    private fun settingPage() {
        setFieldName(false)
    }

    private fun changeFocus(hasFocus: Boolean) {
        if ((!hasFocus) and (newProject.id != 0L)) {
            setFieldName(false)
            binding.tvNameProject.text =
                binding.edNameProject.text.ifBlank { binding.tvNameProject.text }
        }
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

    private fun pickImages() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestPermission
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imagePickerLauncher.launch(intent)
        }
    }

    private fun pickPdf() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                requestPermission
            )
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/pdf"
            }
            pdfPickerLauncher.launch(intent)
        }
    }

    private fun changeSpeed(hasFocus: Boolean) {
        if (!hasFocus) {
            with(binding) {
                try {
                    val value = edSpeed.text.toString().toInt()
                    if ((0 <= value) and (value <= 300)) {
                        sbSpeed.progress = value
                        edSpeed.hint = value.toString()
                    } else if ((300 < value) and (value < 1000)) {
                        sbSpeed.progress = 300
                        edSpeed.hint = value.toString()
                    } else {
                        edSpeed.setText(edSpeed.hint)
                    }
                } catch (e: Exception) {
                    edSpeed.setText(edSpeed.hint)
                }
            }
        }
    }

    private fun saveProjectChange() {
        changeSpeed(false)
        changeFocus(false)
        val nameEd = binding.edNameProject.text.toString().trim()
        val speed = binding.edSpeed.text.toString().ifEmpty { binding.edSpeed.hint.toString() }
        viewModel.save(nameEd, speed.toInt(), newProject)
    }

    private fun runProject() {
        viewModel.runProject(
            { saveProjectChange() },
            { runProjectViewModel() }
        )
    }

    private fun runProjectViewModel() {
        val direction =
            ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                projectId = newProject.id,
                projectName = newProject.name,
                projectSpeed = newProject.speed
            )
        navigator().navigateTo(direction)
    }

    private fun deleteProject() {
        viewModel.deleteProject(newProject.id) { navigator().toMenu() }
        navigator().toMenu()
    }

    inner class Seekbar : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            binding.edSpeed.hint = progress.toString()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            binding.edSpeed.setText("")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            val value = seekBar?.progress ?: 100
            binding.edSpeed.setText(value.toString())
        }

    }
}