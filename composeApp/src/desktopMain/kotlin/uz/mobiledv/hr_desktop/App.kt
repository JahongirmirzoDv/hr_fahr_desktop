package uz.mobiledv.hr_desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.navigation.Screen
import uz.mobiledv.hr_desktop.screens.AuthViewModel
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceScreen
import uz.mobiledv.hr_desktop.screens.auth.LoginScreen
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardScreen
import uz.mobiledv.hr_desktop.screens.employee.EmployeeScreen
import uz.mobiledv.ui.HRDesktopTheme

@Composable
fun App(authViewModel: AuthViewModel = koinInject()) {
    val navController = rememberNavController()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    HRDesktopTheme {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.MainRoute.route else Screen.AuthRoute.route
        ) {
            composable(Screen.AuthRoute.route) {
                LoginScreen(navController)
            }
            composable(Screen.MainRoute.route) {
                MainScreen(authViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route
    val currentUser by authViewModel.uiState.collectAsState()

    Row(Modifier.fillMaxSize()) {
        // Navigation Rail
        NavigationRail(
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(Modifier.height(16.dp))

            // User Info
            currentUser.currentUser?.let { user ->
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = user.fullName,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                    Text(
                        text = user.role,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val mainScreens = getMainScreensForRole(currentUser.currentUser?.role ?: "EMPLOYEE")

            mainScreens.forEach { screen ->
                NavigationRailItem(
                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    selected = currentDestination == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            Spacer(Modifier.weight(1f))

            // Logout Button
            NavigationRailItem(
                icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
                label = { Text("Logout") },
                selected = false,
                onClick = { authViewModel.logout() }
            )

            Spacer(Modifier.height(16.dp))
        }

        // Main Content
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.MainScreen.Dashboard.route
            ) {
                composable(Screen.MainScreen.Dashboard.route) {
                    DashboardScreen()
                }
                composable(Screen.MainScreen.Employees.route) {
                    EmployeeScreen()
                }
                composable(Screen.MainScreen.Attendance.route) {
                    AttendanceScreen()
                }
                composable(Screen.MainScreen.Users.route) {
                    UserScreen()
                }
                composable(Screen.MainScreen.Salary.route) {
                    SalaryScreen()
                }
                composable(Screen.MainScreen.Projects.route) {
                    ProjectScreen()
                }
                composable(Screen.MainScreen.Reports.route) {
                    ReportScreen()
                }
                composable(Screen.MainScreen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}

fun getMainScreensForRole(role: String): List<Screen.MainScreen> {
    return listOf(
        Screen.MainScreen.Dashboard,
        Screen.MainScreen.Employees,
        Screen.MainScreen.Users,
        Screen.MainScreen.Attendance,
        Screen.MainScreen.Salary,
        Screen.MainScreen.Projects,
        Screen.MainScreen.Reports,
        Screen.MainScreen.Settings
    )

}