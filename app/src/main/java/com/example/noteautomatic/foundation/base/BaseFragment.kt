package com.example.noteautomatic.foundation.base

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment

abstract class BaseFragment(private val layoutId: Int) : Fragment(layoutId) {

    abstract val viewModel: BaseViewModel
    fun <T> renderResult(
        root: View,
        result: AppResult<T>,
        onSuccess: (T) -> Unit,
        onPending: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        (root as ViewGroup).children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is PendingResult -> onPending()
            is ErrorResult -> onError(result.exception)
        }
    }
}