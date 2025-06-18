package uz.mobiledv.hr_desktop.data.remote.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import uz.mobiledv.hr_desktop.utils.DateUtil


enum class SalaryType {
    MONTHLY, DAILY, HOURLY
}


@Serializable
data class EmployeeDto(
    val id: String,
    val userId: String,
    val name: String,
    val email: String,
    val position: String,
    val department: String,
    val hireDate: String,
    val salaryType: SalaryType,
    val salaryAmount: Double,
    val isActive: Boolean,
    val createdAt: LocalDateTime = DateUtil.datetimeInUtc,
    val updatedAt: LocalDateTime = DateUtil.datetimeInUtc,
    val salaryRate: Double? = null
)