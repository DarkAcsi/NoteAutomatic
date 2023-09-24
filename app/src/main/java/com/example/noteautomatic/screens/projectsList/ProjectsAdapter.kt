package com.example.noteautomatic.screens.projectsList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemProjectBinding

interface ProjectActionListener {

    fun onProjectSetting(project: Project)

    fun onProjectDelete(project: Project)

    fun onProjectSelect(project: Project)

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
    RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>(), View.OnClickListener {

    var projects: List<Project> = emptyList()
        set(newValue) {
            val diffCallback = ProjectDiffCallback(field, newValue)
            val diffResult =
                DiffUtil.calculateDiff(diffCallback) // false вторым аргументом - убрать анимацию
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val project = v.tag as Project
        when (v.id) {
            R.id.ivActions -> {
                actionListener.onProjectPlay(project)
            }

            else -> {
                actionListener.onProjectSetting(project)
            }
        }
    }

    override fun getItemCount(): Int = projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProjectBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.ivActions.setOnClickListener(this)

        return ProjectsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = projects[position]
        holder.itemView.tag = project
        with(holder.binding) {
            tvItemProject.text = project.name
            ivActions.tag = project
        }
    }


    class ProjectsViewHolder(
        val binding: ItemProjectBinding
    ) : RecyclerView.ViewHolder(binding.root)

}