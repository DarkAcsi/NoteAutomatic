package com.example.noteautomatic.model

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteautomatic.R
import com.example.noteautomatic.databinding.ItemProjectBinding

interface ProjectActionListener {

    fun onProjectRename(project: Project)

    fun onProjectMoveUp(project: Project)

    fun onProjectDelete(project: Project)

    fun onProjectDetail(project: Project)

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
            val diffResult = DiffUtil.calculateDiff(diffCallback) // false вторым аргументом - убрать анимацию
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onClick(v: View) {
        val project = v.tag as Project
        when (v.id) {
            R.id.ivMore -> {
                showPopupMenu(v)
            }
            else -> {
                actionListener.onProjectDetail(project)
            }
        }
    }

    override fun getItemCount(): Int = projects.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProjectBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.ivMore.setOnClickListener(this)
//        binding.tvItemProject.setOnClickListener(this)

        return ProjectsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        val project = projects[position]
        holder.itemView.tag = project
        with(holder.binding) {
            tvItemProject.text = project.name
            ivMore.tag = project
//            tvItemProject.tag = project
        }
    }

    private fun showPopupMenu(view: View){
        val popupMenu = PopupMenu(view.context, view)
        val project = view.tag as Project
        val position = projects.indexOfFirst { it.id == project.id }

        popupMenu.menu.add(0, ID_RENAME, Menu.NONE, "Rename")
        popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Move up")
            .apply { isEnabled = position > 0 }
        popupMenu.menu.add(0, ID_DELETE, Menu.NONE, "Delete")

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_RENAME -> {
                    actionListener.onProjectRename(project)
                }
                ID_MOVE_UP -> {
                    actionListener.onProjectMoveUp(project)
                }
                ID_DELETE -> {
                    actionListener.onProjectDelete(project)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    class ProjectsViewHolder(
        val binding: ItemProjectBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object{
        private const val ID_RENAME = 1
        private const val ID_MOVE_UP = 2
        private const val ID_DELETE = 3
    }
}