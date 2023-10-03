package com.example.noteautomatic

import android.os.Bundle
import androidx.navigation.NavDirections

interface Navigator {

    fun navigateUp()

    fun navigateTo(direction: NavDirections)

    fun navigateTo(destination: Int, args: Bundle? = null)

    fun toMenu()

    fun toast(message: String)

}