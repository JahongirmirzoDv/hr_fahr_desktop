package uz.mobiledv.hr_desktop.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.remote.repository.EmployeeService
import uz.mobiledv.hr_desktop.utils.AuthSettings

class DashboardViewModel(
    private val employeeService: EmployeeService,
    private val authSettings: AuthSettings
) : ViewModel() {

    fun logOut() {
        authSettings.clearAuthSettings()
    }
}