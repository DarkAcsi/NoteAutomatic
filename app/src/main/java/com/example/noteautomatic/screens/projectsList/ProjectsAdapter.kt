package com.example.noteautomatic.screens.projectsList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemProjectBinding
import com.example.noteautomatic.foundation.database.entities.Project
import com.example.noteautomatic.screens.ProjectDiffCallback

interface ProjectActionListener {

    fun onProjectSetting(project: Project)

    fun onProjectsSelect(project: Project, selected: Boolean)

    fun onProjectSelectMore(project: Project): Boolean

    fun onProjectPlay(project: Project)

}

class ProjectsAdapter(private val actionListener: ProjectActionListener) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>(),
    View.OnClickListener, View.OnLongClickListener {

    private var selected = false

    var projects: List<Project> = emptyList()
        set(newValue) {
            val diffCallback = ProjectDiffCallback(field, newValue)
            val diffResult =
                DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View?) {
        val project = v?.tag as Project
        when (v.id) {
            R.id.ivPlay -> {
                if (!selected) {
                    actionListener.onProjectPlay(project)
                } else {
                    selected = actionListener.onProjectSelectMore(project)
                }
            }

            else -> {
                if (!selected) {
                    actionListener.onProjectSetting(project)
                } else {
                    selected = actionListener.onProjectSelectMore(project)
                }
            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        val project = v?.tag as Project
        if (!selected) {
            selected = true
            actionListener.onProjectsSelect(project, this.selected)
            return true
        }
        return false
    }

    override fun getItemCount(): Int = projects.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProjectBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
        binding.ivPlay.setOnClickListener(this)

        return ProjectsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = projects[position]
        holder.itemView.tag = project
        with(holder.binding) {
            tvItemProject.text = project.name
            ivPlay.tag = project
            when (project.selected) {
                true -> {
                    ivPlay.setImageResource(android.R.drawable.checkbox_on_background)
                }

                false -> {
                    ivPlay.setImageResource(android.R.drawable.checkbox_off_background)
                }

                else -> {
                    ivPlay.setImageResource(android.R.drawable.ic_media_play)
                }
            }
        }
    }

    class ProjectsViewHolder(
        val binding: ItemProjectBinding
    ) : RecyclerView.ViewHolder(binding.root)

}