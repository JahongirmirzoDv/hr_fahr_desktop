package uz.mobiledv.hr_desktop.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.data.model.Employee
import uz.mobiledv.hr_desktop.data.model.Project
import uz.mobiledv.hr_desktop.screens.components.ErrorMessage
import uz.mobiledv.hr_desktop.screens.components.LoadingIndicator

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Welcome to HR Management System",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { viewModel.refresh() },
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Refresh")
            }
        }

        Spacer(Modifier.height(24.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error ?: "",
                    onRetry = { viewModel.loadDashboardData() }
                )
            }
            uiState.dashboardData != null -> {
                val data = uiState.dashboardData

                data?.let { data ->
                    // Statistics Cards
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            StatCard(
                                title = "Total Employees",
                                value = data.totalEmployees.toString(),
                                subtitle = "${data.activeEmployees} active",
                                icon = Icons.Default.People,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        item {
                            StatCard(
                                title = "Total Projects",
                                value = data.totalProjects.toString(),
                                subtitle = "${data.activeProjects} active",
                                icon = Icons.Default.Work,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        item {
                            StatCard(
                                title = "Pending Salaries",
                                value = data.pendingSalaries.toString(),
                                subtitle = "To be processed",
                                icon = Icons.Default.AttachMoney,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        item {
                            StatCard(
                                title = "Attendance Rate",
                                value = "${String.format("%.1f", data.monthlyAttendanceRate)}%",
                                subtitle = "This month",
                                icon = Icons.Default.TrendingUp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Recent Data Sections
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Recent Employees
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Recent Employees",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(16.dp))

                            if (data?.recentEmployees?.isEmpty() ?: true) {
                                Text(
                                    text = "No employees found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.height(200.dp)
                                ) {
                                    items(data.recentEmployees) { employee ->
                                        RecentEmployeeItem(employee)
                                    }
                                }
                            }
                        }
                    }

                    // Recent Projects
                    Card(
                        modifier = Modifier.weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Recent Projects",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(16.dp))

                            if (data?.recentProjects?.isEmpty() ?: true) {
                                Text(
                                    text = "No projects found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.height(200.dp)
                                ) {
                                    items(data.recentProjects) { project ->
                                        RecentProjectItem(project)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(200.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun RecentEmployeeItem(employee: Employee) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = employee.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${employee.position} • ${employee.department}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Surface(
            color = if (employee.isActive) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (employee.isActive) "Active" else "Inactive",
                style = MaterialTheme.typography.labelSmall,
                color = if (employee.isActive) Color.Green else Color.Red,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun RecentProjectItem(project: Project) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = project.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = project.location ?: "No location",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Surface(
            color = when (project.status) {
                "ACTIVE" -> Color.Green.copy(alpha = 0.2f)
                "COMPLETED" -> Color.Blue.copy(alpha = 0.2f)
                "PAUSED" -> Color.Magenta.copy(alpha = 0.2f)
                else -> Color.Gray.copy(alpha = 0.2f)
            },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = project.status,
                style = MaterialTheme.typography.labelSmall,
                color = when (project.status) {
                    "ACTIVE" -> Color.Green
                    "COMPLETED" -> Color.Blue
                    "PAUSED" -> Color.Magenta
                    else -> Color.Gray
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}