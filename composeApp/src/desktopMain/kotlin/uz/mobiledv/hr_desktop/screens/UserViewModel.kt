package uz.mobiledv.hr_desktop.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.remote.repository.UserService

class UserViewModel(
    private val userService: UserService
) : ViewModel() {
    fun register(name: String, email: String) = viewModelScope.launch {
        val success = userService.registerUser(name, email)
        if (success) {
            // update UI
        }
    }
}
