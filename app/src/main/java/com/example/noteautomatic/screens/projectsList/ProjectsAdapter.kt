package com.example.noteautomatic.screens.projectsList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemProjectBinding

interface ProjectActionListener {

    fun onProjectSetting(project: Project)

    fun onProjectsSelect(project: Project, selected: Boolean)

    fun onProjectSelectMore(project: Project): Boolean

    fun onProjectPlay(project: Project)

}

class ProjectDiffCallback(
    private val oldList: List<Project>,
    private val newList: List<Project>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition] // Project - data class
    }
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
                    Log.d("adapte", "onProjectPlay")
                    actionListener.onProjectPlay(project)
                }
                else {
                    selected = actionListener.onProjectSelectMore(project)
                    Log.d("adapte","onProjectSelectMore $selected")
                }
            }
            else -> {
                if (!selected) {
                    Log.d("adapte", "onProjectSetting")
                    actionListener.onProjectSetting(project)
                } else {
                    selected = actionListener.onProjectSelectMore(project)
                    Log.d("adapte","onProjectSelectMore $selected")
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
            ivCheck.tag = project
            ivUncheck.tag = project
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