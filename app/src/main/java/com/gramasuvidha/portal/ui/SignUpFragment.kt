package com.gramasuvidha.portal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gramasuvidha.portal.R
import com.gramasuvidha.portal.databinding.FragmentSignupBinding
import com.gramasuvidha.portal.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val authVM: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authVM.user.observe(viewLifecycleOwner) { user ->
            if (user != null) findNavController().navigate(R.id.action_signUp_to_projectList)
        }

        authVM.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.signupProgress.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnSignUp.isEnabled = !loading
        }

        authVM.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                authVM.clearError()
            }
        }

        binding.btnSignUp.setOnClickListener {
            val name     = binding.etName.text.toString().trim()
            val email    = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirm  = binding.etConfirmPassword.text.toString()
            when {
                name.isBlank() ->
                    Toast.makeText(requireContext(), "Enter your name", Toast.LENGTH_SHORT).show()
                password != confirm ->
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                else -> authVM.signUp(email, password, name)
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
