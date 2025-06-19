package uz.mobiledv.hr_desktop.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.model.User
import uz.mobiledv.hr_desktop.data.model.UserCreateRequest
import uz.mobiledv.hr_desktop.repository.AuthRepository
import uz.mobiledv.hr_desktop.utils.AuthManager

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val currentUser: User? = null,
    val error: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authRepository.login(email, password)

            if (result.isSuccess) {
                val loginResponse = result.getOrThrow()

                // Save user data to AuthManager (additional save if needed)
                authManager.saveAuthData(loginResponse.token, loginResponse.user)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentUser = loginResponse.user
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    fun register(userRequest: UserCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authRepository.register(userRequest)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                // Auto-login after successful registration
                login(userRequest.email, userRequest.password)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Registration failed"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Additional method to update user data in AuthManager
    fun updateUserData(user: User) {
        authManager.updateUser(user)
        _uiState.value = _uiState.value.copy(currentUser = user)
    }

    // Method to get current user from AuthManager
    fun getCurrentUser(): User? {
        return authManager.getCurrentUser()
    }
}