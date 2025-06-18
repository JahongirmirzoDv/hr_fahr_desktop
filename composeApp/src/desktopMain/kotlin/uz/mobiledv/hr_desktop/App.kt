package uz.mobiledv.hr_desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import uz.mobiledv.hr_desktop.navigation.Screen
import uz.mobiledv.hr_desktop.navigation.mainScreens
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceScreen
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardScreen
import uz.mobiledv.hr_desktop.screens.employee.EmployeeScreen
import uz.mobiledv.hr_desktop.screens.report.ReportsScreen
import uz.mobiledv.ui.HRDesktopTheme

@Composable
fun App() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    HRDesktopTheme {
        Row(Modifier.fillMaxSize()) {
            NavigationRail {
                mainScreens.forEach { screen ->
                    NavigationRailItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentScreen.route == screen.route,
                        onClick = {
                            currentScreen = screen
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        }
                    )
                }
            }

            NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Employees.route) { EmployeeScreen() }
                composable(Screen.Attendance.route) { AttendanceScreen() }
                composable(Screen.Reports.route) { ReportsScreen() }
                composable(Screen.Settings.route) { /* Settings Screen UI */ }
            }
        }
    }
}