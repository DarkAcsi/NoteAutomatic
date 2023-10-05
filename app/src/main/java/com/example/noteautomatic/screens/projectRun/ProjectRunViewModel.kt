package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MediatorLiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.base.PendingResult
import com.example.noteautomatic.foundation.base.SuccessResult
import com.example.noteautomatic.foundation.database.entities.Image
import com.example.noteautomatic.foundation.model.image.ImagesListener
import com.example.noteautomatic.foundation.model.image.ImagesRepository
import kotlinx.coroutines.launch

class ProjectRunViewModel(
    private val imagesRepository: ImagesRepository
) : BaseViewModel() {

    private var _imagesFiles = MediatorLiveResult<List<Image>>(PendingResult())
    val imagesFiles: LiveResult<List<Image>> = _imagesFiles
    private var _images = MutableLiveResult<List<Image>>()

    private val listener: ImagesListener = {
        _images.postValue(SuccessResult(it))
    }

    init {
        imagesRepository.addListener(listener)
        _imagesFiles.addSource(_images) { mergeSources() }
    }

    fun loadImages(id: Long) {
        viewModelScope.launch{
            _images.postValue(
                SuccessResult(imagesRepository.loadImages(id)))
        }
    }


    private fun mergeSources() {
        val images = _images.value ?: return
        var imageList = mutableListOf<Image>()
        images.map {list ->
            list.forEach{
                if (it.countPages == -1)
                    imageList.add(it)
                else for (i in 0 until it.countPages)
                    imageList.add(it.copy(countPages = i))
            }
        }
        _imagesFiles.value = SuccessResult(imageList)
    }

    override fun onCleared() {
        super.onCleared()
        imagesRepository.removeListener(listener)
    }

    fun tryAgain() {
        _imagesFiles.value?.map{
            loadImages(it[0].projectId)
        }
    }
}