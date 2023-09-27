package com.example.noteautomatic.screens.projectCreation

import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.noteautomatic.R
import com.example.noteautomatic.Repositories
import com.example.noteautomatic.databinding.FragmentProjectCreationBinding
import com.example.noteautomatic.navigator
import com.example.noteautomatic.viewModelCreator
import java.io.File

class ProjectCreationFragment : Fragment(R.layout.fragment_project_creation) {

    private lateinit var binding: FragmentProjectCreationBinding

    private val viewModel by viewModelCreator { ProjectCreationViewModel(Repositories.projectsRepository) }

    private val args: ProjectCreationFragmentArgs by navArgs()
    private var newProject = true

    private var imageUris: MutableList<Uri> = mutableListOf()
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            imageUris.clear()
            imageUris.addAll(uris)
        }

    private var pagesUris: MutableList<Uri> = mutableListOf()
    private val pickFiles =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { documentUri ->
            documentUri?.let { uri ->
//                viewModel.fullProject.value?.file = uri

                val pdfFile = uri.path?.let { File(it) }
                val pdfDocument = PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY))
                val pageCount = pdfDocument.pageCount
                for (i in 0 until pageCount) {
                    val page = pdfDocument.openPage(i)
                    val link = Uri.parse("file://${pdfFile?.absolutePath}#page=${i + 1}")
                    pagesUris.add(link)
                    page.close()
                }
                pdfDocument.close()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProjectCreationBinding.bind(view)
        newProject = args.projectId == 0L
        navigator().onToolbarVisibilityChanged(true)
        with(binding) {
            setFieldName(false)
            viewModel.fullProject.observe(viewLifecycleOwner) {
                tvNameProject.text = it?.name
                btnToRun.isEnabled = !it?.listImage.isNullOrEmpty()
                navigator().renameToolbar(it?.name.toString())
            }
        }

        settingPage()

        with(binding) {

            btnRename.setOnClickListener { setFieldName(true) }

            btnAddImage.setOnClickListener { pickImage() }

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
        with(binding) {
            setFieldName(false)
            viewModel.fullProject.observe(viewLifecycleOwner) {
                tvNameProject.text = it?.name
                btnToRun.isEnabled = !it?.listImage.isNullOrEmpty()
                navigator().renameToolbar(it?.name.toString())
            }
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
                    if (newProject)
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
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickFiles.launch(arrayOf(intent.toString()))
    }

    private fun saveProjectChange() {
        val speed = binding.edSpeed.text.toString().ifEmpty { binding.edSpeed.hint.toString() }

        var name = binding.tvNameProject.text.toString()
        val projectName = viewModel.setNameProject(binding.edNameProject.text.toString(), name, newProject)
        if (projectName.isBlank()) {
            if (newProject)
                return
        }
        name = projectName
        if (viewModel.save(name, speed.toInt(), newProject) == null)
            navigator().toMenu()
        settingPage()
    }

    private fun runProject() {
        viewModel.fullProject.value?.let {
            val projectId = it.id
            val projectName = it.name
            val direction =
                ProjectCreationFragmentDirections.actionProjectCreationFragmentToProjectRunFragment(
                    projectId = projectId,
                    projectName = projectName
                )
            navigator().navigateTo(direction)
        }
    }
}