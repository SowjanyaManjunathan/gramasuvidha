package com.gramasuvidha.portal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.databinding.FragmentProjectListBinding
import com.gramasuvidha.portal.firebase.FCMTestHelper
import com.gramasuvidha.portal.viewmodel.AuthViewModel
import com.gramasuvidha.portal.viewmodel.ProjectViewModel

class ProjectListFragment : Fragment() {

    private var _binding: FragmentProjectListBinding? = null
    private val binding get() = _binding!!
    private val projectVM: ProjectViewModel by activityViewModels()
    private val authVM: AuthViewModel by activityViewModels()
    private lateinit var adapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupListeners()
        FCMTestHelper.setup(requireActivity())
    }

    private fun setupRecyclerView() {
        adapter = ProjectAdapter(isKannada = false) { project ->
            projectVM.selectProject(project)
            findNavController().navigate(R.id.action_list_to_detail)
        }
        binding.rvProjects.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProjects.adapter = adapter
    }

    private fun observeViewModel() {
        projectVM.projects.observe(viewLifecycleOwner) { projects ->
            adapter.submitList(projects)
            binding.tvEmptyState.visibility = if (projects.isEmpty()) View.VISIBLE else View.GONE
        }

        projectVM.isKannada.observe(viewLifecycleOwner) { isKannada ->
            adapter.setLanguage(isKannada)
            binding.tvAppTitle.text  = if (isKannada) "ಗ್ರಾಮ ಸುವಿಧಾ ಪೋರ್ಟಲ್" else "Grama-Suvidha Portal"
            binding.tvSubtitle.text  = if (isKannada) "ಡಿಜಿಟಲ್ ನೋಟಿಸ್ ಬೋರ್ಡ್" else "Digital Notice Board"
            binding.btnLanguage.text = if (isKannada) "English" else "ಕನ್ನಡ"
        }

        projectVM.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        projectVM.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                projectVM.clearError()
            }
        }
    }

    private fun setupListeners() {
        binding.btnLanguage.setOnClickListener { projectVM.toggleLanguage() }

        binding.btnLogout.setOnClickListener {
            authVM.logout()
            findNavController().navigate(R.id.action_list_to_login)
        }

        binding.btnSeedDb.setOnClickListener { projectVM.seedDatabase() }

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group.findViewById<Chip>(checkedIds.firstOrNull() ?: -1)
            val status = when (chip?.tag?.toString()) {
                "ongoing"   -> "Ongoing"
                "completed" -> "Completed"
                "planned"   -> "Planned"
                else        -> null
            }
            projectVM.filterByStatus(status)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
