package com.gramasuvidha.portal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.gramasuvidha.portal.firebase.AuthRepository
import com.gramasuvidha.portal.firebase.FCMTokenRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _user    = MutableLiveData<FirebaseUser?>(AuthRepository.currentUser)
    val user: LiveData<FirebaseUser?> get() = _user

    private val _loading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _loading

    private val _error   = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    val isLoggedIn: Boolean get() = AuthRepository.isLoggedIn

    fun signUp(email: String, password: String, name: String) {
        if (!validate(email, password)) return
        _loading.value = true
        viewModelScope.launch {
            AuthRepository.signUp(email, password, name)
                .onSuccess { user ->
                    _user.value = user
                    FCMTokenRepository.refreshAndSave()
                    FCMTokenRepository.subscribeToUpdates()
                }
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun login(email: String, password: String) {
        if (!validate(email, password)) return
        _loading.value = true
        viewModelScope.launch {
            AuthRepository.login(email, password)
                .onSuccess { user ->
                    _user.value = user
                    FCMTokenRepository.refreshAndSave()
                    FCMTokenRepository.subscribeToUpdates()
                }
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun logout() {
        AuthRepository.logout()
        _user.value = null
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            AuthRepository.sendPasswordReset(email)
                .onSuccess { _error.value = "✓ Reset email sent!" }
                .onFailure { _error.value = it.message }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        if (email.isBlank() || !email.contains("@")) {
            _error.value = "Enter a valid email."
            return false
        }
        if (password.length < 6) {
            _error.value = "Password must be at least 6 characters."
            return false
        }
        return true
    }

    fun clearError() { _error.value = null }
}
