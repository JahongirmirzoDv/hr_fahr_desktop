package uz.mobiledv.hr_desktop.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.data.model.SalaryCalculationRequest
import uz.mobiledv.hr_desktop.data.model.SalaryRecord
import uz.mobiledv.hr_desktop.repository.SalaryRepository

data class SalaryUiState(
    val salaryRecords: List<SalaryRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SalaryViewModel(
    private val salaryRepository: SalaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalaryUiState())
    val uiState: StateFlow<SalaryUiState> = _uiState.asStateFlow()

    init {
        loadSalaryRecords()
    }

    fun loadSalaryRecords() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = salaryRepository.getAllSalaries()
            
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    salaryRecords = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load salary records"
                )
            }
        }
    }

    fun calculateSalary(request: SalaryCalculationRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = salaryRepository.calculateSalary(request)
            
            if (result.isSuccess) {
                loadSalaryRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to calculate salary"
                )
            }
        }
    }

    fun updatePaymentStatus(id: String, status: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = salaryRepository.updateSalaryPaymentStatus(id, status)
            
            if (result.isSuccess) {
                loadSalaryRecords() // Refresh the list
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to update payment status"
                )
            }
        }
    }

    fun getSalaryHistory(employeeId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = salaryRepository.getSalaryHistory(employeeId)
            
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    salaryRecords = result.getOrThrow(),
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to load salary history"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}