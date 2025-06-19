package uz.mobiledv.hr_desktop.screens.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.model.Attendance
import uz.mobiledv.hr_desktop.data.model.AttendanceCreateRequest
import uz.mobiledv.hr_desktop.repository.AttendanceRepository

data class AttendanceUiState(
    val attendanceRecords: List<Attendance> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val hasNextPage: Boolean = false,
    val hasPreviousPage: Boolean = false
)

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    init {
        loadAttendanceRecords()
    }

    fun loadAttendanceRecords(page: Int = 1, pageSize: Int = 20) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.getAllAttendance(page, pageSize)

            if (result.isSuccess) {
                val paginatedResponse = result.getOrThrow()
                _uiState.value = _uiState.value.copy(
                    attendanceRecords = paginatedResponse.data,
                    currentPage = paginatedResponse.page,
                    totalPages = paginatedResponse.totalPages,
                    hasNextPage = paginatedResponse.page < paginatedResponse.totalPages,
                    hasPreviousPage = paginatedResponse.page > 1,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load attendance records"
                )
            }
        }
    }

    fun loadEmployeeAttendance(employeeId: String, startDate: String? = null, endDate: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.getAttendanceByEmployee(employeeId, startDate, endDate)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    attendanceRecords = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load employee attendance"
                )
            }
        }
    }

    fun createAttendanceRecord(attendance: AttendanceCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.createAttendance(attendance)

            if (result.isSuccess) {
                loadAttendanceRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to create attendance record"
                )
            }
        }
    }

    fun updateAttendanceRecord(id: String, attendance: AttendanceCreateRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.updateAttendance(id, attendance)

            if (result.isSuccess) {
                loadAttendanceRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to update attendance record"
                )
            }
        }
    }

    fun deleteAttendanceRecord(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.deleteAttendance(id)

            if (result.isSuccess) {
                loadAttendanceRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to delete attendance record"
                )
            }
        }
    }

    fun checkIn(employeeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.checkIn(employeeId)

            if (result.isSuccess) {
                loadAttendanceRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to check in"
                )
            }
        }
    }

    fun checkOut(employeeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = attendanceRepository.checkOut(employeeId)

            if (result.isSuccess) {
                loadAttendanceRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to check out"
                )
            }
        }
    }

    fun nextPage() {
        if (_uiState.value.hasNextPage) {
            loadAttendanceRecords(_uiState.value.currentPage + 1)
        }
    }

    fun previousPage() {
        if (_uiState.value.hasPreviousPage) {
            loadAttendanceRecords(_uiState.value.currentPage - 1)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}