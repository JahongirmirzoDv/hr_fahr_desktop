package uz.mobiledv.hr_desktop.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object AuthRoute : Screen("auth")
    object MainRoute : Screen("main")

    sealed class MainScreen(
        route: String,
        val label: String,
        val icon: ImageVector
    ) : Screen(route) {
        object Dashboard : MainScreen("dashboard", "Dashboard", Icons.Default.Dashboard)
        object Employees : MainScreen("employees", "Employees", Icons.Default.People)
        object Users : MainScreen("users", "Users", Icons.Default.PersonAdd)
        object Attendance : MainScreen("attendance", "Attendance", Icons.Default.DateRange)
        object Salary : MainScreen("salary", "Salary", Icons.Default.AttachMoney)
        object Projects : MainScreen("projects", "Projects", Icons.Default.Work)
        object Reports : MainScreen("reports", "Reports", Icons.Default.Assessment)
        object Settings : MainScreen("settings", "Settings", Icons.Default.Settings)
    }

    object Login : Screen("login")
}