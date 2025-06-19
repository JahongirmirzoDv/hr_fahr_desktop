package uz.mobiledv.hr_desktop.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.model.Project
import uz.mobiledv.hr_desktop.data.model.ProjectCreateRequest
import uz.mobiledv.hr_desktop.repository.ProjectRepository

data class ProjectUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedProject: Project? = null
)

class ProjectViewModel(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = projectRepository.getAllProjects()
            
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    projects = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load projects"
                )
            }
        }
    }

    fun createProject(project: ProjectCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = projectRepository.createProject(project)
            
            if (result.isSuccess) {
                loadProjects() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to create project"
                )
            }
        }
    }

    fun updateProject(id: String, project: ProjectCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = projectRepository.updateProject(id, project)
            
            if (result.isSuccess) {
                loadProjects() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to update project"
                )
            }
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = projectRepository.deleteProject(id)
            
            if (result.isSuccess) {
                loadProjects() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to delete project"
                )
            }
        }
    }

    fun selectProject(project: Project) {
        _uiState.value = _uiState.value.copy(selectedProject = project)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedProject = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}