package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.Employee
import uz.mobiledv.hr_desktop.data.model.EmployeeCreateRequest
import uz.mobiledv.hr_desktop.data.network.ApiService

class EmployeeRepository(
    private val apiService: ApiService
) {
    suspend fun getAllEmployees(): Result<List<Employee>> {
        return apiService.getAllEmployees()
    }

    suspend fun getEmployeeById(id: String): Result<Employee> {
        return apiService.getEmployeeById(id)
    }

    suspend fun getEmployeesByDepartment(department: String): Result<List<Employee>> {
        return apiService.getEmployeesByDepartment(department)
    }

    suspend fun createEmployee(employee: EmployeeCreateRequest, photo: ByteArray): Result<Employee> {
        return apiService.createEmployee(employee, photo)
    }

    suspend fun updateEmployee(id: String, employee: EmployeeCreateRequest): Result<Employee> {
        return apiService.updateEmployee(id, employee)
    }

    suspend fun deleteEmployee(id: String): Result<Boolean> {
        return apiService.deleteEmployee(id)
    }
}