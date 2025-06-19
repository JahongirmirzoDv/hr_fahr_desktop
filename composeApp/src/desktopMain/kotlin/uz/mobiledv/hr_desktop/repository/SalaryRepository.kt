package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.SalaryCalculationRequest
import uz.mobiledv.hr_desktop.data.model.SalaryRecord
import uz.mobiledv.hr_desktop.data.network.ApiService

class SalaryRepository(
    private val apiService: ApiService
) {
    suspend fun getAllSalaries(): Result<List<SalaryRecord>> {
        return apiService.getAllSalaries()
    }

    suspend fun getSalaryHistory(employeeId: String): Result<List<SalaryRecord>> {
        return apiService.getSalaryHistory(employeeId)
    }

    suspend fun calculateSalary(request: SalaryCalculationRequest): Result<SalaryRecord> {
        return apiService.calculateSalary(request)
    }

    suspend fun updateSalaryPaymentStatus(id: String, status: String): Result<SalaryRecord> {
        return apiService.updateSalaryPaymentStatus(id, status)
    }

    suspend fun getUserSalary(): Result<Map<String, Any>> {
        return apiService.getUserSalary()
    }
}