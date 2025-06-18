package uz.mobiledv.hr_desktop.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = koinInject()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard("Total Employees", "250", Modifier.weight(1f))
            SummaryCard("Active Users", "220", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Attendance Trends", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}