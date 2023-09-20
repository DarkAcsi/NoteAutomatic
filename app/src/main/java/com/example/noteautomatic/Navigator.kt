package com.example.noteautomatic

import com.example.noteautomatic.screens.projectsList.projects.Project

interface Navigator {

    fun openProject(project: Project)

    fun goBack()

    fun toast(messageRes: Int)

}