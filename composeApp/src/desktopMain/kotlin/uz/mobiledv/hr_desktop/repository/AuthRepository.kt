package uz.mobiledv.hr_desktop.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.mobiledv.hr_desktop.data.model.LoginRequest
import uz.mobiledv.hr_desktop.data.model.LoginResponse
import uz.mobiledv.hr_desktop.data.model.User
import uz.mobiledv.hr_desktop.data.model.UserCreateRequest
import uz.mobiledv.hr_desktop.data.network.ApiService
import uz.mobiledv.hr_desktop.utils.AuthManager

class AuthRepository(
    private val apiService: ApiService,
    private val authManager: AuthManager
) {
    private val _isLoggedIn = MutableStateFlow(authManager.isLoggedIn())
    val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(authManager.getCurrentUser())
    val currentUser: Flow<User?> = _currentUser.asStateFlow()

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val result = apiService.login(LoginRequest(email, password))
            if (result.isSuccess) {
                val loginResponse = result.getOrThrow()
                authManager.saveAuthData(loginResponse.token, loginResponse.user)
                _isLoggedIn.value = true
                _currentUser.value = loginResponse.user
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(userRequest: UserCreateRequest): Result<User> {
        return apiService.register(userRequest)
    }

    suspend fun logout() {
        authManager.clearAuthData()
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    suspend fun getUserProfile(): Result<User> {
        return try {
            val result = apiService.getUserProfile()
            if (result.isSuccess) {
                val user = result.getOrThrow()
                authManager.updateUser(user)
                _currentUser.value = user
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}