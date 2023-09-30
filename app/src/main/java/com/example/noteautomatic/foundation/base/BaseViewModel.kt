package com.example.noteautomatic.foundation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteautomatic.foundation.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<AppResult<T>>
typealias MutableLiveResult<T> = MutableLiveData<AppResult<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<AppResult<T>>


open class BaseViewModel : ViewModel() {
    open fun onResult(result: Any) {

    }

}