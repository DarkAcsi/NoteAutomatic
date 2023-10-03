package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
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

    private var _images = MutableLiveResult<List<Image>>(PendingResult())
    val images: LiveResult<List<Image>> = _images

    private val listener: ImagesListener = {
        _images.postValue(SuccessResult(it))
    }

    init {
        imagesRepository.addListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        imagesRepository.removeListener(listener)
    }

    fun loadImages(id: Long) {
        viewModelScope.launch{
            _images.postValue(
                SuccessResult(imagesRepository.loadImages(id))
            )
        }
    }



}