package com.gramasuvidha.portal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.Project
import com.gramasuvidha.portal.databinding.ItemProjectBinding

class ProjectAdapter(
    private var isKannada: Boolean,
    private val onProjectClick: (Project) -> Unit
) : ListAdapter<Project, ProjectAdapter.ProjectViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(a: Project, b: Project) = a.id == b.id
            override fun areContentsTheSame(a: Project, b: Project) = a == b
        }
    }

    inner class ProjectViewHolder(private val binding: ItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {
            binding.tvProjectTitle.text  = if (isKannada) project.titleKn else project.titleEn
            binding.tvCategory.text      = project.category
            binding.tvWard.text          = project.ward
            binding.tvBudget.text        = project.budget
            binding.progressBar.progress = project.progress
            binding.tvProgress.text      = "${project.progress}%"
            binding.tvStatus.text        = project.status

            val ctx = binding.root.context
            val (bgColor, txtColor) = when (project.status) {
                "Completed" -> R.color.statusCompletedBg to R.color.statusCompletedText
                "Ongoing"   -> R.color.statusOngoingBg  to R.color.statusOngoingText
                else        -> R.color.statusPlannedBg  to R.color.statusPlannedText
            }
            binding.tvStatus.backgroundTintList = ContextCompat.getColorStateList(ctx, bgColor)
            binding.tvStatus.setTextColor(ContextCompat.getColor(ctx, txtColor))

            binding.root.setOnClickListener { onProjectClick(project) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setLanguage(kannada: Boolean) {
        isKannada = kannada
        notifyDataSetChanged()
    }
}
