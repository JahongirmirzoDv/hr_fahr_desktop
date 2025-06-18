package uz.mobiledv.hr_desktop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.material.icons.filled.*
sealed class Screen(val route: String) {

    // Top-level routes for navigation structure
    object AuthRoute : Screen("auth_route")
    object MainRoute : Screen("main_route")

    // Visible screens in the NavigationRail
    sealed class MainScreen(
        route: String,
        val label: String,
        val icon: ImageVector
    ) : Screen(route) {
        object Dashboard : MainScreen("dashboard", "Dashboard", Icons.Default.Dashboard)
        object Employees : MainScreen("employees", "Employees", Icons.Default.People)
        object Attendance : MainScreen("attendance", "Attendance", Icons.Default.DateRange)
        object Reports : MainScreen("reports", "Reports", Icons.Default.Assessment)
        object Settings : MainScreen("settings", "Settings", Icons.Default.Settings)
    }

    // Login screen
    object Login : Screen("login")
}


val mainScreens = listOf(
    Screen.MainScreen.Dashboard,
    Screen.MainScreen.Employees,
    Screen.MainScreen.Attendance,
    Screen.MainScreen.Reports,
    Screen.MainScreen.Settings
)