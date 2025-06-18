package uz.mobiledv.hr_desktop.di

import org.koin.dsl.module
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceViewModel
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardViewModel
import uz.mobiledv.hr_desktop.screens.employee.EmployeeViewModel
import uz.mobiledv.hr_desktop.screens.report.ReportViewModel

val appModule = module {
    single { DashboardViewModel() }
    single { EmployeeViewModel() }
    single { AttendanceViewModel() }
    single { ReportViewModel() }
}