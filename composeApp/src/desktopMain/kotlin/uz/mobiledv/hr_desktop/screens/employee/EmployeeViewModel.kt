package uz.mobiledv.hr_desktop.screens.employee

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.remote.model.EmployeeDto
import uz.mobiledv.hr_desktop.data.remote.repository.EmployeeService

class EmployeeViewModel(
    private val employeeService: EmployeeService
) : ViewModel() {

    var allEmployees = mutableStateOf<List<EmployeeDto>>(emptyList())

    init {
        getAllEmployees()
    }

    fun getAllEmployees() {
        viewModelScope.launch(Dispatchers.IO) {
            allEmployees.value = employeeService.getAll()
        }
    }
}
