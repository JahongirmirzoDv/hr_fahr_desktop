package uz.mobiledv.hr_desktop.screens.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.model.Employee
import uz.mobiledv.hr_desktop.data.model.EmployeeCreateRequest
import uz.mobiledv.hr_desktop.repository.EmployeeRepository

data class EmployeeUiState(
    val employees: List<Employee> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedEmployee: Employee? = null
)

class EmployeeViewModel(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmployeeUiState())
    val uiState: StateFlow<EmployeeUiState> = _uiState.asStateFlow()

    fun loadEmployees() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.getAllEmployees()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    employees = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load employees"
                )
            }
        }
    }

    fun createEmployee(employee: EmployeeCreateRequest, photo: ByteArray) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.createEmployee(employee, photo)

            if (result.isSuccess) {
                loadEmployees() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to create employee"
                )
            }
        }
    }

    fun updateEmployee(id: String, employee: EmployeeCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.updateEmployee(id, employee)

            if (result.isSuccess) {
                loadEmployees() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to update employee"
                )
            }
        }
    }

    fun deleteEmployee(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.deleteEmployee(id)

            if (result.isSuccess) {
                loadEmployees() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to delete employee"
                )
            }
        }
    }

    fun getEmployeesByDepartment(department: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.getEmployeesByDepartment(department)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    employees = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load employees"
                )
            }
        }
    }

    fun selectEmployee(employee: Employee) {
        _uiState.value = _uiState.value.copy(selectedEmployee = employee)
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedEmployee = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}