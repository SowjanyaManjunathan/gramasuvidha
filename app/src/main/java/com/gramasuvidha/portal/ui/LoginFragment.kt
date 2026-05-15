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
import com.gramasuvidha.portal.databinding.FragmentLoginBinding
import com.gramasuvidha.portal.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authVM: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (authVM.isLoggedIn) {
            findNavController().navigate(R.id.action_login_to_projectList)
            return
        }

        authVM.user.observe(viewLifecycleOwner) { user ->
            if (user != null) findNavController().navigate(R.id.action_login_to_projectList)
        }

        authVM.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loginProgress.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !loading
        }

        authVM.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                authVM.clearError()
            }
        }

        binding.btnLogin.setOnClickListener {
            authVM.login(
                email    = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString()
            )
        }

        binding.btnGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signUp)
        }

        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isBlank()) {
                Toast.makeText(requireContext(), "Enter your email first", Toast.LENGTH_SHORT).show()
            } else {
                authVM.sendPasswordReset(email)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
