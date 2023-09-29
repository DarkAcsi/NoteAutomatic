package com.example.noteautomatic.screens.gallery
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.example.noteautomatic.R
//import com.example.noteautomatic.databinding.FragmentFilePickerBinding
//
//class FilePickerFragment : Fragment() {
//
//    private lateinit var imageUriList: MutableList<Uri>
//    private lateinit var documentUriList: MutableList<Uri>
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val binding = FragmentFilePickerBinding.inflate(inflater, container, false)
//        imageUriList = mutableListOf()
//        documentUriList = mutableListOf()
//        binding.buttonPickImage.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), REQUEST_PICK_IMAGE)
//        }
//        binding.buttonPickDocument.setOnClickListener {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.type = "*/*"
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            startActivityForResult(Intent.createChooser(intent, "Select Document(s)"), REQUEST_PICK_DOCUMENT)
//        }
//        binding.buttonSave.setOnClickListener {
//            saveFileUriList()
//            navigateToImageGalleryFragment()
//        }
//        return binding.root
//    }
//
//    private fun saveFileUriList() {
//        // Сохраняем список URI-адресов в базе данных или SharedPreferences
//        // каждый uri будет отдельным файлом, не связанным с общим документом
//    }
//
//    private fun navigateToImageGalleryFragment() {
//        findNavController().navigate(R.id.action_filePickerFragment_to_imageGalleryFragment)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                REQUEST_PICK_IMAGE -> {
//                    data?.clipData?.let {
//                        for (i in 0 until it.itemCount) {
//                            val uri = it.getItemAt(i).uri
//                            imageUriList.add(uri)
//                        }
//                    } ?: run {
//                        val uri = data?.data
//                        uri?.let { imageUriList.add(it) }
//                    }
//                }
//                REQUEST_PICK_DOCUMENT -> {
//                    data?.clipData?.let {
//                        for (i in 0 until it.itemCount) {
//                            val uri = it.getItemAt(i).uri
//                            documentUriList.add(uri)
//                        }
//                    } ?: run {
//                        val uri = data?.data
//                        uri?.let { documentUriList.add(it) }
//                    }
//                }
//            }
//        }
//    }
//
//    companion object {
//
//        private val REQUEST_PICK_IMAGE = 1
//        private val REQUEST_PICK_DOCUMENT = 2
//
//    }
//}
