package com.example.noteautomatic.foundation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

typealias LiveResult<T> = LiveData<AppResult<T>>
typealias MutableLiveResult<T> = MutableLiveData<AppResult<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<AppResult<T>>


open class BaseViewModel : ViewModel()