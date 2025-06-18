package uz.mobiledv.hr_desktop.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.remote.repository.AuthRepository
import uz.mobiledv.hr_desktop.data.remote.repository.AuthService
import uz.mobiledv.hr_desktop.utils.AuthSettings

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val authSettings: AuthSettings
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


    init {
        viewModelScope.launch {
            val storedUser = authSettings.getCurrentUser()
            _isLoggedIn.value = storedUser != null
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(username, password)
            _isLoggedIn.value = result.token.isNotBlank()
            if (_isLoggedIn.value) {
                authSettings.saveCurrentUser(result)
            }
        }
    }
}