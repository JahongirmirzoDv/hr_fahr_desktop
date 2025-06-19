package uz.mobiledv.hr_desktop.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

enum class SalaryType {
    MONTHLY, DAILY, HOURLY
}

@Serializable
data class Employee(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val position: String,
    val department: String,
    val hireDate: String,
    val salaryType: SalaryType,
    val salaryAmount: Double,
    val salaryRate: Double? = null,
    val isActive: Boolean,
    val faceEmbedding: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Serializable
data class EmployeeCreateRequest(
    val userId: String,
    val name: String,
    val position: String,
    val department: String,
    val salaryType: SalaryType,
    val salaryAmount: Double,
    val salaryRate: Double? = null,
    val isActive: Boolean = true
)

@Serializable
data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val role: String,
)

@Serializable
data class UserCreateRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val role: String
)

@Serializable
data class Attendance(
    val id: String,
    val employeeId: String,
    val date: String,
    val checkIn: String?,
    val checkOut: String?,
    val status: String,
    val notes: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Serializable
data class AttendanceCreateRequest(
    val employeeId: String,
    val date: String,
    val checkIn: String? = null,
    val checkOut: String? = null,
    val status: String,
    val notes: String? = null
)

@Serializable
data class SalaryRecord(
    val id: String,
    val employeeId: String,
    val periodStart: String,
    val periodEnd: String,
    val baseAmount: Double,
    val bonus: Double,
    val deductions: Double,
    val netAmount: Double,
    val paymentStatus: String,
    val paymentDate: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Serializable
data class SalaryCalculationRequest(
    val employeeId: String,
    val periodStart: String,
    val periodEnd: String,
    val bonus: Double = 0.0,
    val deductions: Double = 0.0
)

@Serializable
data class Project(
    val id: String,
    val name: String,
    val description: String,
    val location: String?,
    val startDate: String,
    val endDate: String?,
    val managerId: String?,
    val employeeIds: List<String>,
    val budget: Double?,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

@Serializable
data class ProjectCreateRequest(
    val name: String,
    val description: String? = null,
    val location: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val managerId: String? = null,
    val employeeIds: List<String> = emptyList(),
    val budget: Double? = null,
    val status: String = "ACTIVE"
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: User
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

@Serializable
data class PaginatedResponse<T>(
    val data: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int
)