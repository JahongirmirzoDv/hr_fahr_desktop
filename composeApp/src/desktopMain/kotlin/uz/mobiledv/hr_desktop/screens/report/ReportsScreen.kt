package uz.mobiledv.hr_desktop.screens.report

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

data class ReportTemplate(
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun ReportsScreen(viewModel: ReportViewModel = koinInject()) {
    val reportTemplates = listOf(
        ReportTemplate("Performance Reviews", "Assess individual and team performance", Icons.Default.TrendingUp),
        ReportTemplate("Employee Directory", "Manage employee information and contact details", Icons.Default.Contacts),
        ReportTemplate("Attendance Summaries", "Track employee attendance and absences", Icons.Default.EventNote),
        ReportTemplate("Time Tracking", "Monitor time spent on tasks and projects", Icons.Default.Schedule),
        ReportTemplate("Task Completion", "Review completed tasks and milestones", Icons.Default.CheckCircleOutline),
        ReportTemplate("Project Progress", "Evaluate project progress and timelines", Icons.Default.DonutLarge)
    )

    var selectedTabIndex by remember { mutableStateOf(1) } // Templates is selected by default

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Reports", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Generate and view reports on employee performance, attendance, and project progress.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(Modifier.height(24.dp))

        TabRow(selectedTabIndex = selectedTabIndex, containerColor = MaterialTheme.colorScheme.background) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Text("Overview", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Text("Templates", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTabIndex == 2, onClick = { selectedTabIndex = 2 }) {
                Text("Scheduled", modifier = Modifier.padding(16.dp))
            }
        }

        Spacer(Modifier.height(24.dp))

        when (selectedTabIndex) {
            1 -> { // Templates Tab
                Column {
                    Text("Report Templates", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 200.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(reportTemplates) { template ->
                            ReportTemplateCard(template)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("Custom Reports", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    CustomReportCard()
                }
            }
            // Other tabs can be implemented here
            0 -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Overview content goes here")
                }
            }
            2 -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("Scheduled reports content goes here")
                }
            }
        }
    }
}

@Composable
fun ReportTemplateCard(template: ReportTemplate) {
    Card(
        modifier = Modifier.height(150.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(template.icon, contentDescription = template.title, modifier = Modifier.size(32.dp))
            Text(template.title, style = MaterialTheme.typography.titleMedium)
            Text(template.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun CustomReportCard() {
    val stroke = Stroke(width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    //77 051 41 21
    Box(
        Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(
                shape = RoundedCornerShape(12.dp),
                width = 1.dp,
                color = Color.Gray
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Create a custom report", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Text("Build a report from scratch with the data you need.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, textAlign = TextAlign.Center)
            Button(onClick = {}) {
                Text("New Report")
            }
        }
    }
}
