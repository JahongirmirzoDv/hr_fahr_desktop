package uz.mobiledv.hr_desktop.screens.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun ReportsScreen(viewModel: ReportViewModel = koinInject()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reports", style = MaterialTheme.typography.headlineMedium)
        // Add Report templates UI
    }
}