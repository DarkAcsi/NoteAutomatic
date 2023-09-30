package com.example.noteautomatic.screens

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.PartResultBinding
import com.example.noteautomatic.foundation.base.AppResult
import com.example.noteautomatic.foundation.base.BaseFragment

fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: AppResult<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)

    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.clLoading.visibility = View.VISIBLE
        },
        onError = {
            binding.clError.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.clLoading && it.id != R.id.clError }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        }
    )
}

fun BaseFragment.onTryAgain(root: View, onTryAgainPressed: () -> Unit) {
    root.findViewById<Button>(R.id.btnTryAgain).setOnClickListener { onTryAgainPressed() }
}