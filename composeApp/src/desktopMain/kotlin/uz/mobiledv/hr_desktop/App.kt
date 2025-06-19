package uz.mobiledv.hr_desktop

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.navigation.Screen
import uz.mobiledv.hr_desktop.navigation.mainScreens
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceScreen
import uz.mobiledv.hr_desktop.screens.auth.AuthViewModel
import uz.mobiledv.hr_desktop.screens.auth.LoginScreen
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardScreen
import uz.mobiledv.hr_desktop.screens.employee.EmployeeScreen
import uz.mobiledv.hr_desktop.screens.report.ReportsScreen
import uz.mobiledv.ui.HRDesktopTheme

@Composable
fun App(loginViewModel: AuthViewModel = koinInject()) {
    val navController = rememberNavController()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    HRDesktopTheme {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) Screen.MainRoute.route else Screen.AuthRoute.route
        ) {
            composable(Screen.AuthRoute.route) {
                LoginScreen(navController)
            }
            composable(Screen.MainRoute.route) {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    Row(Modifier.fillMaxSize()) {
        NavigationRail {
            mainScreens.forEach { screen ->
                NavigationRailItem(
                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    selected = currentDestination == screen.route,
                    onClick = {
                        if (currentDestination != screen.route) {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationRoute ?: screen.route) {
                                    saveState = true
                                }
                            }
                        }
                    }
                )
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.Dashboard.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.MainScreen.Dashboard.route) { DashboardScreen() }
            composable(Screen.MainScreen.Employees.route) { EmployeeScreen() }
            composable(Screen.MainScreen.Attendance.route) { AttendanceScreen() }
            composable(Screen.MainScreen.Reports.route) { ReportsScreen() }
            composable(Screen.MainScreen.Settings.route) { /* TODO: SettingsScreen() */ }
        }
    }
}
