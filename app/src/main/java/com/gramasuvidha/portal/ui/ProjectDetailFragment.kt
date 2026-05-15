package com.gramasuvidha.portal.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.data.Project
import com.gramasuvidha.portal.databinding.FragmentProjectDetailBinding
import com.gramasuvidha.portal.viewmodel.ProjectViewModel

class ProjectDetailFragment : Fragment() {

    private var _binding: FragmentProjectDetailBinding? = null
    private val binding get() = _binding!!
    private val projectVM: ProjectViewModel by activityViewModels()
    private var pendingPhotoType: String = "before"

    private val photoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@registerForActivityResult
            val project = projectVM.selectedProject.value ?: return@registerForActivityResult
            projectVM.uploadPhoto(requireContext(), project.firestoreId, uri, pendingPhotoType)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        val isKannada = projectVM.isKannada.value ?: false

        projectVM.selectedProject.observe(viewLifecycleOwner) { project ->
            project ?: return@observe
            renderProject(project, isKannada)
        }

        projectVM.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.uploadProgress.visibility = if (loading) View.VISIBLE else View.GONE
        }

        projectVM.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                projectVM.clearError()
            }
        }
    }

    private fun renderProject(project: Project, isKannada: Boolean) {
        binding.tvDetailTitle.text       = if (isKannada) project.titleKn else project.titleEn
        binding.tvDetailDescription.text = if (isKannada) project.descriptionKn else project.descriptionEn
        binding.tvDetailStatus.text      = project.status
        binding.tvDetailBudget.text      = project.budget
        binding.tvDetailCompletion.text  = project.expectedCompletion
        binding.tvDetailContractor.text  = project.contractor
        binding.tvDetailWard.text        = project.ward
        binding.tvDetailCategory.text    = project.category
        binding.detailProgressBar.progress = project.progress
        binding.tvDetailProgress.text    = "${project.progress}% Complete"
        binding.ratingBar.rating         = project.userRating.toFloat()

        val ctx = requireContext()
        val (bg, txt) = when (project.status) {
            "Completed" -> R.color.statusCompletedBg to R.color.statusCompletedText
            "Ongoing"   -> R.color.statusOngoingBg   to R.color.statusOngoingText
            else        -> R.color.statusPlannedBg   to R.color.statusPlannedText
        }
        binding.tvDetailStatus.backgroundTintList = ContextCompat.getColorStateList(ctx, bg)
        binding.tvDetailStatus.setTextColor(ContextCompat.getColor(ctx, txt))

        binding.btnReportIssue.isEnabled = !project.issueReported
        binding.btnReportIssue.text = if (project.issueReported) "✓ Issue Reported" else "Report Issue"

        // Load Before photo from Base64
        if (project.beforePhotoBase64.isNotBlank()) {
            try {
                val bytes = Base64.decode(project.beforePhotoBase64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.ivBeforePhoto.setImageBitmap(bmp)
                binding.ivBeforePhoto.visibility = View.VISIBLE
            } catch (e: Exception) { /* skip if corrupt */ }
        }

        // Load After photo from Base64
        if (project.afterPhotoBase64.isNotBlank()) {
            try {
                val bytes = Base64.decode(project.afterPhotoBase64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.ivAfterPhoto.setImageBitmap(bmp)
                binding.ivAfterPhoto.visibility = View.VISIBLE
            } catch (e: Exception) { /* skip if corrupt */ }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSubmitRating.setOnClickListener {
            val project = projectVM.selectedProject.value ?: return@setOnClickListener
            val rating = binding.ratingBar.rating.toInt()
            if (rating == 0) {
                Toast.makeText(requireContext(), "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            projectVM.submitRating(project.id, rating)
        }

        binding.btnReportIssue.setOnClickListener {
            val project = projectVM.selectedProject.value ?: return@setOnClickListener
            projectVM.reportIssue(project.id, "Issue reported by citizen via app")
        }

        binding.btnUploadBefore.setOnClickListener {
            pendingPhotoType = "before"
            openGallery()
        }

        binding.btnUploadAfter.setOnClickListener {
            pendingPhotoType = "after"
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        photoPickerLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
