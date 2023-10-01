package com.example.noteautomatic.screens

import android.os.Bundle
import androidx.navigation.NavDirections

interface Navigator {

    fun navigateTo(direction: NavDirections)

    fun navigateTo(destination: Int, args: Bundle? = null)

    fun toMenu()

    fun onToolbarVisibilityChanged(visible: Boolean)

    fun renameToolbar(name: String)

    fun toast(message: String)

}