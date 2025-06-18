package uz.mobiledv.hr_desktop.screens.attendance


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun AttendanceScreen(viewModel: AttendanceViewModel = koinInject()) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Attendance", style = MaterialTheme.typography.headlineMedium)
        // Add Calendar and summary UI here
    }
}