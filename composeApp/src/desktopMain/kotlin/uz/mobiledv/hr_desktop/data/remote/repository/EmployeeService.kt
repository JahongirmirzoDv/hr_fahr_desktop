package uz.mobiledv.hr_desktop.data.remote.repository

import uz.mobiledv.hr_desktop.data.remote.model.EmployeeDto

interface EmployeeService {
    suspend fun getAll(): List<EmployeeDto>
    suspend fun create(employee: EmployeeDto): Boolean
    suspend fun delete(id: String): Boolean
}
