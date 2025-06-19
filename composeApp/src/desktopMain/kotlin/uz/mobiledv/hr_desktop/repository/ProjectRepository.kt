package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.Project
import uz.mobiledv.hr_desktop.data.model.ProjectCreateRequest
import uz.mobiledv.hr_desktop.data.network.ApiService

class ProjectRepository(
    private val apiService: ApiService
) {
    suspend fun getAllProjects(): Result<List<Project>> {
        return apiService.getAllProjects()
    }

    suspend fun getProjectById(id: String): Result<Project> {
        return apiService.getProjectById(id)
    }

    suspend fun createProject(project: ProjectCreateRequest): Result<Project> {
        return apiService.createProject(project)
    }

    suspend fun updateProject(id: String, project: ProjectCreateRequest): Result<Project> {
        return apiService.updateProject(id, project)
    }

    suspend fun deleteProject(id: String): Result<Boolean> {
        return apiService.deleteProject(id)
    }
}