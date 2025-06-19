package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.Employee
import uz.mobiledv.hr_desktop.data.model.Attendance
import uz.mobiledv.hr_desktop.data.model.Project
import uz.mobiledv.hr_desktop.data.model.SalaryRecord
import uz.mobiledv.hr_desktop.data.network.ApiService

class DashboardRepository(
    private val apiService: ApiService
) {
    suspend fun getDashboardData(): Result<DashboardData> {
        return try {
            val employeesResult = apiService.getAllEmployees()
            val attendanceResult = apiService.getAllAttendance()
            val projectsResult = apiService.getAllProjects()
            val salariesResult = apiService.getAllSalaries()

            if (employeesResult.isSuccess && attendanceResult.isSuccess && 
                projectsResult.isSuccess && salariesResult.isSuccess) {
                
                val employees = employeesResult.getOrThrow()
                val attendance = attendanceResult.getOrThrow()
                val projects = projectsResult.getOrThrow()
                val salaries = salariesResult.getOrThrow()

                Result.success(
                    DashboardData(
                        totalEmployees = employees.size,
                        activeEmployees = employees.count { it.isActive },
                        totalProjects = projects.size,
                        activeProjects = projects.count { it.status == "ACTIVE" },
                        pendingSalaries = salaries.count { it.paymentStatus == "PENDING" },
                        monthlyAttendanceRate = calculateAttendanceRate(attendance.data),
                        recentEmployees = employees.sortedByDescending { it.createdAt }.take(5),
                        recentProjects = projects.sortedByDescending { it.createdAt }.take(5)
                    )
                )
            } else {
                Result.failure(Exception("Failed to load dashboard data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateAttendanceRate(attendance: List<Attendance>): Double {
        if (attendance.isEmpty()) return 0.0
        val presentCount = attendance.count { it.status == "PRESENT" }
        return (presentCount.toDouble() / attendance.size) * 100
    }
}

data class DashboardData(
    val totalEmployees: Int,
    val activeEmployees: Int,
    val totalProjects: Int,
    val activeProjects: Int,
    val pendingSalaries: Int,
    val monthlyAttendanceRate: Double,
    val recentEmployees: List<Employee>,
    val recentProjects: List<Project>
)