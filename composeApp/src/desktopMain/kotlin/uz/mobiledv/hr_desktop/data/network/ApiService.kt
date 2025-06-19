package uz.mobiledv.hr_desktop.data.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import uz.mobiledv.hr_desktop.data.model.*

interface ApiService {
    // Auth endpoints
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse>
    suspend fun register(userRequest: UserCreateRequest): Result<User>
    suspend fun getUserProfile(): Result<User>
    
    // Employee endpoints
    suspend fun getAllEmployees(): Result<List<Employee>>
    suspend fun getEmployeeById(id: String): Result<Employee>
    suspend fun getEmployeesByDepartment(department: String): Result<List<Employee>>
    suspend fun createEmployee(employee: EmployeeCreateRequest, photo: ByteArray): Result<Employee>
    suspend fun updateEmployee(id: String, employee: EmployeeCreateRequest): Result<Employee>
    suspend fun deleteEmployee(id: String): Result<Boolean>
    
    // User management endpoints
    suspend fun getAllUsers(): Result<List<User>>
    suspend fun getUserById(id: String): Result<User>
    suspend fun createUser(user: UserCreateRequest): Result<User>
    suspend fun updateUser(id: String, user: UserCreateRequest): Result<User>
    suspend fun deleteUser(id: String): Result<Boolean>
    
    // Attendance endpoints
    suspend fun getAllAttendance(page: Int = 1, pageSize: Int = 20): Result<PaginatedResponse<Attendance>>
    suspend fun getAttendanceByEmployee(employeeId: String, startDate: String? = null, endDate: String? = null): Result<List<Attendance>>
    suspend fun createAttendance(attendance: AttendanceCreateRequest): Result<Attendance>
    suspend fun updateAttendance(id: String, attendance: AttendanceCreateRequest): Result<Attendance>
    suspend fun deleteAttendance(id: String): Result<Boolean>
    suspend fun checkIn(employeeId: String): Result<Attendance>
    suspend fun checkOut(employeeId: String): Result<Attendance>
    
    // Salary endpoints
    suspend fun getAllSalaries(): Result<List<SalaryRecord>>
    suspend fun getSalaryHistory(employeeId: String): Result<List<SalaryRecord>>
    suspend fun calculateSalary(request: SalaryCalculationRequest): Result<SalaryRecord>
    suspend fun updateSalaryPaymentStatus(id: String, status: String): Result<SalaryRecord>
    
    // Project endpoints
    suspend fun getAllProjects(): Result<List<Project>>
    suspend fun getProjectById(id: String): Result<Project>
    suspend fun createProject(project: ProjectCreateRequest): Result<Project>
    suspend fun updateProject(id: String, project: ProjectCreateRequest): Result<Project>
    suspend fun deleteProject(id: String): Result<Boolean>
    
    // User specific endpoints
    suspend fun getUserAttendance(): Result<Map<String, Any>>
    suspend fun getUserSalary(): Result<Map<String, Any>>
}

class ApiServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : ApiService {
    
    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
            Result.success(response.body<LoginResponse>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun register(userRequest: UserCreateRequest): Result<User> {
        return try {
            val response = client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(userRequest)
            }
            val apiResponse = response.body<ApiResponse<User>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserProfile(): Result<User> {
        return try {
            val response = client.get("$baseUrl/user/profile")
            Result.success(response.body<User>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllEmployees(): Result<List<Employee>> {
        return try {
            val response = client.get("$baseUrl/admin/employees")
            Result.success(response.body<List<Employee>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEmployeeById(id: String): Result<Employee> {
        return try {
            val response = client.get("$baseUrl/admin/employees/$id")
            Result.success(response.body<Employee>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getEmployeesByDepartment(department: String): Result<List<Employee>> {
        return try {
            val response = client.get("$baseUrl/admin/employees/department/$department")
            Result.success(response.body<List<Employee>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createEmployee(employee: EmployeeCreateRequest, photo: ByteArray): Result<Employee> {
        return try {
            val response = client.submitFormWithBinaryData(
                url = "$baseUrl/admin/employees",
                formData = formData {
                    append("userId", employee.userId)
                    append("name", employee.name)
                    append("position", employee.position)
                    append("department", employee.department)
                    append("salaryType", employee.salaryType.name)
                    append("salaryAmount", employee.salaryAmount.toString())
                    employee.salaryRate?.let { append("salaryRate", it.toString()) }
                    append("isActive", employee.isActive.toString())
                    append("photo", photo, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"photo.jpg\"")
                    })
                }
            )
            val apiResponse = response.body<ApiResponse<Employee>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error ?: "Employee creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateEmployee(id: String, employee: EmployeeCreateRequest): Result<Employee> {
        return try {
            val response = client.put("$baseUrl/admin/employees/$id") {
                contentType(ContentType.Application.Json)
                setBody(employee)
            }
            Result.success(response.body<Employee>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteEmployee(id: String): Result<Boolean> {
        return try {
            val response = client.delete("$baseUrl/admin/employees/$id")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val response = client.get("$baseUrl/admin/users")
            Result.success(response.body<List<User>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserById(id: String): Result<User> {
        return try {
            val response = client.get("$baseUrl/admin/users/$id")
            Result.success(response.body<User>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createUser(user: UserCreateRequest): Result<User> {
        return try {
            val response = client.post("$baseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val apiResponse = response.body<ApiResponse<User>>()
            if (apiResponse.success && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse.error ?: "User creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(id: String, user: UserCreateRequest): Result<User> {
        return try {
            val response = client.put("$baseUrl/admin/users/$id") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            Result.success(response.body<User>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteUser(id: String): Result<Boolean> {
        return try {
            val response = client.delete("$baseUrl/admin/users/$id")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllAttendance(page: Int, pageSize: Int): Result<PaginatedResponse<Attendance>> {
        return try {
            val response = client.get("$baseUrl/admin/attendance") {
                parameter("page", page)
                parameter("pageSize", pageSize)
            }
            Result.success(response.body<PaginatedResponse<Attendance>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAttendanceByEmployee(employeeId: String, startDate: String?, endDate: String?): Result<List<Attendance>> {
        return try {
            val response = client.get("$baseUrl/admin/attendance") {
                parameter("employeeId", employeeId)
                startDate?.let { parameter("startDate", it) }
                endDate?.let { parameter("endDate", it) }
            }
            Result.success(response.body<List<Attendance>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createAttendance(attendance: AttendanceCreateRequest): Result<Attendance> {
        return try {
            val response = client.post("$baseUrl/admin/attendance") {
                contentType(ContentType.Application.Json)
                setBody(attendance)
            }
            Result.success(response.body<Attendance>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateAttendance(id: String, attendance: AttendanceCreateRequest): Result<Attendance> {
        return try {
            val response = client.put("$baseUrl/admin/attendance/$id") {
                contentType(ContentType.Application.Json)
                setBody(attendance)
            }
            Result.success(response.body<Attendance>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteAttendance(id: String): Result<Boolean> {
        return try {
            val response = client.delete("$baseUrl/admin/attendance/$id")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkIn(employeeId: String): Result<Attendance> {
        return try {
            val response = client.post("$baseUrl/manager/attendance/check-in") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("employeeId" to employeeId))
            }
            Result.success(response.body<Attendance>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkOut(employeeId: String): Result<Attendance> {
        return try {
            val response = client.post("$baseUrl/manager/attendance/check-out") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("employeeId" to employeeId))
            }
            Result.success(response.body<Attendance>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllSalaries(): Result<List<SalaryRecord>> {
        return try {
            val response = client.get("$baseUrl/admin/salaries")
            Result.success(response.body<List<SalaryRecord>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getSalaryHistory(employeeId: String): Result<List<SalaryRecord>> {
        return try {
            val response = client.get("$baseUrl/admin/salaries/history/$employeeId")
            Result.success(response.body<List<SalaryRecord>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun calculateSalary(request: SalaryCalculationRequest): Result<SalaryRecord> {
        return try {
            val response = client.post("$baseUrl/admin/salaries/calculate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body<SalaryRecord>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateSalaryPaymentStatus(id: String, status: String): Result<SalaryRecord> {
        return try {
            val response = client.put("$baseUrl/admin/salaries/$id/status") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("status" to status))
            }
            Result.success(response.body<SalaryRecord>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllProjects(): Result<List<Project>> {
        return try {
            val response = client.get("$baseUrl/admin/projects")
            Result.success(response.body<List<Project>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getProjectById(id: String): Result<Project> {
        return try {
            val response = client.get("$baseUrl/admin/projects/$id")
            Result.success(response.body<Project>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createProject(project: ProjectCreateRequest): Result<Project> {
        return try {
            val response = client.post("$baseUrl/admin/projects") {
                contentType(ContentType.Application.Json)
                setBody(project)
            }
            Result.success(response.body<Project>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateProject(id: String, project: ProjectCreateRequest): Result<Project> {
        return try {
            val response = client.put("$baseUrl/admin/projects/$id") {
                contentType(ContentType.Application.Json)
                setBody(project)
            }
            Result.success(response.body<Project>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteProject(id: String): Result<Boolean> {
        return try {
            val response = client.delete("$baseUrl/admin/projects/$id")
            Result.success(response.status == HttpStatusCode.OK)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserAttendance(): Result<Map<String, Any>> {
        return try {
            val response = client.get("$baseUrl/user/attendance")
            Result.success(response.body<Map<String, Any>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserSalary(): Result<Map<String, Any>> {
        return try {
            val response = client.get("$baseUrl/user/salary")
            Result.success(response.body<Map<String, Any>>())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}