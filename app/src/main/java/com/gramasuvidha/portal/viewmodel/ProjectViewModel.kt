package com.gramasuvidha.portal.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gramasuvidha.portal.data.MockDataSource
import com.gramasuvidha.portal.data.Project
import com.gramasuvidha.portal.firebase.FeedbackRepository
import com.gramasuvidha.portal.firebase.ProjectRepository
import com.gramasuvidha.portal.firebase.StorageRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {

    private val _projects        = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> get() = _projects

    private val _selectedProject = MutableLiveData<Project?>()
    val selectedProject: LiveData<Project?> get() = _selectedProject

    private val _isKannada       = MutableLiveData(false)
    val isKannada: LiveData<Boolean> get() = _isKannada

    private val _isLoading       = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error           = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var allProjects: List<Project> = emptyList()

    init {
        startObservingFirestore()
    }

    private fun startObservingFirestore() {
        _isLoading.value = true
        ProjectRepository.observeProjects()
            .onEach { list ->
                allProjects = list
                _projects.value = list
                _isLoading.value = false
            }
            .catch {
                // Offline fallback — show mock data
                allProjects = MockDataSource.getProjects()
                _projects.value = allProjects
                _isLoading.value = false
                _error.value = "Offline — showing sample data"
            }
            .launchIn(viewModelScope)
    }

    fun seedDatabase() {
        viewModelScope.launch {
            _isLoading.value = true
            ProjectRepository.seedMockProjects()
                .onSuccess { _error.value = "✓ Database seeded!" }
                .onFailure { _error.value = "Seed failed: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun selectProject(project: Project) {
        _selectedProject.value = project
    }

    fun filterByStatus(status: String?) {
        _projects.value = if (status == null) allProjects
        else allProjects.filter { it.status == status }
    }

    fun toggleLanguage() {
        _isKannada.value = !(_isKannada.value ?: false)
    }

    fun submitRating(projectId: Int, rating: Int) {
        viewModelScope.launch {
            FeedbackRepository.submitRating(projectId.toString(), rating)
                .onSuccess {
                    _selectedProject.value = _selectedProject.value?.copy(userRating = rating)
                    _error.value = "✓ Rating submitted!"
                }
                .onFailure { _error.value = "Rating failed: ${it.message}" }
        }
    }

    fun reportIssue(projectId: Int, description: String) {
        viewModelScope.launch {
            FeedbackRepository.reportIssue(projectId.toString(), description)
                .onSuccess {
                    _selectedProject.value = _selectedProject.value?.copy(issueReported = true)
                    _error.value = "✓ Issue reported!"
                }
                .onFailure { _error.value = "Report failed: ${it.message}" }
        }
    }

    fun uploadPhoto(context: Context, projectId: String, uri: Uri, type: String) {
        viewModelScope.launch {
            _isLoading.value = true
            StorageRepository.uploadProjectPhoto(context, projectId, uri, type)
                .onSuccess { _error.value = "✓ Photo saved!" }
                .onFailure { _error.value = "Photo failed: ${it.message}" }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
