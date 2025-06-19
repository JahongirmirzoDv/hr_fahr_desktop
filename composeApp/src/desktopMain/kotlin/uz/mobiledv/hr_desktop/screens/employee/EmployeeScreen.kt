package uz.mobiledv.hr_desktop.screens.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.data.remote.model.EmployeeDto

@Composable
fun EmployeeScreen(viewModel: EmployeeViewModel = koinInject()) {
    val employees = viewModel.allEmployees.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Employees", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        // Add Search and Filter UI
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(employees) { employee ->
                EmployeeRow(employee)
            }
        }
    }
}



@Composable
fun EmployeeRow(employee: EmployeeDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(employee.name, modifier = Modifier.weight(1f))
            Text(employee.department, modifier = Modifier.weight(1f))
            Text(employee.department, modifier = Modifier.weight(1f))
            Button(onClick = {}) { Text(employee.position) }
        }
    }
}
