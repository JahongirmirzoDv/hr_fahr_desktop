package uz.mobiledv.hr_desktop.repository

import uz.mobiledv.hr_desktop.data.model.Attendance
import uz.mobiledv.hr_desktop.data.model.AttendanceCreateRequest
import uz.mobiledv.hr_desktop.data.model.PaginatedResponse
import uz.mobiledv.hr_desktop.data.network.ApiService

class AttendanceRepository(
    private val apiService: ApiService
) {
    suspend fun getAllAttendance(page: Int = 1, pageSize: Int = 20): Result<PaginatedResponse<Attendance>> {
        return apiService.getAllAttendance(page, pageSize)
    }

    suspend fun getAttendanceByEmployee(
        employeeId: String, 
        startDate: String? = null, 
        endDate: String? = null
    ): Result<List<Attendance>> {
        return apiService.getAttendanceByEmployee(employeeId, startDate, endDate)
    }

    suspend fun createAttendance(attendance: AttendanceCreateRequest): Result<Attendance> {
        return apiService.createAttendance(attendance)
    }

    suspend fun updateAttendance(id: String, attendance: AttendanceCreateRequest): Result<Attendance> {
        return apiService.updateAttendance(id, attendance)
    }

    suspend fun deleteAttendance(id: String): Result<Boolean> {
        return apiService.deleteAttendance(id)
    }

    suspend fun checkIn(employeeId: String): Result<Attendance> {
        return apiService.checkIn(employeeId)
    }

    suspend fun checkOut(employeeId: String): Result<Attendance> {
        return apiService.checkOut(employeeId)
    }

    suspend fun getUserAttendance(): Result<Map<String, Any>> {
        return apiService.getUserAttendance()
    }
}