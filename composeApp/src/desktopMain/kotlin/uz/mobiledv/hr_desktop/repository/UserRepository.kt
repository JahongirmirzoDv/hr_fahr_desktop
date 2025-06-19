package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.User
import uz.mobiledv.hr_desktop.data.model.UserCreateRequest
import uz.mobiledv.hr_desktop.data.network.ApiService

class UserRepository(
    private val apiService: ApiService
) {
    suspend fun getAllUsers(): Result<List<User>> {
        return apiService.getAllUsers()
    }

    suspend fun getUserById(id: String): Result<User> {
        return apiService.getUserById(id)
    }

    suspend fun createUser(user: UserCreateRequest): Result<User> {
        return apiService.createUser(user)
    }

    suspend fun updateUser(id: String, user: UserCreateRequest): Result<User> {
        return apiService.updateUser(id, user)
    }

    suspend fun deleteUser(id: String): Result<Boolean> {
        return apiService.deleteUser(id)
    }
}