package uz.mobiledv.hr_desktop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Employees : Screen("employees", "Employees", Icons.Default.People)
    object Attendance : Screen("attendance", "Attendance", Icons.Default.DateRange)
    object Reports : Screen("reports", "Reports", Icons.Default.Assessment)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val mainScreens = listOf(
    Screen.Dashboard,
    Screen.Employees,
    Screen.Attendance,
    Screen.Reports,
    Screen.Settings
)