package uz.mobiledv.hr_desktop.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.remote.repository.AuthService

class AuthViewModel(
    private val authService: AuthService
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = authService.login(username, password)
            _isLoggedIn.value = result.token.isNotBlank()

        }
    }
}