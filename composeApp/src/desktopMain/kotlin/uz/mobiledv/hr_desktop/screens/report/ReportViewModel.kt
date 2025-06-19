package uz.mobiledv.hr_desktop.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.repository.AttendanceRepository
import uz.mobiledv.hr_desktop.repository.EmployeeRepository
import uz.mobiledv.hr_desktop.repository.ProjectRepository
import uz.mobiledv.hr_desktop.repository.SalaryRepository

data class ReportUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val employeeReport: List<Any> = emptyList(),
    val attendanceReport: List<Any> = emptyList(),
    val salaryReport: List<Any> = emptyList(),
    val projectReport: List<Any> = emptyList()
)

class ReportViewModel(
    private val employeeRepository: EmployeeRepository,
    private val attendanceRepository: AttendanceRepository,
    private val salaryRepository: SalaryRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    fun generateEmployeeReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = employeeRepository.getAllEmployees()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    employeeReport = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to generate employee report"
                )
            }
        }
    }

    fun generateAttendanceReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.getAllAttendance()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    attendanceReport = result.getOrThrow().data,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to generate attendance report"
                )
            }
        }
    }

    fun generateSalaryReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = salaryRepository.getAllSalaries()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    salaryReport = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to generate salary report"
                )
            }
        }
    }

    fun generateProjectReport() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = projectRepository.getAllProjects()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    projectReport = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to generate project report"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}