package uz.mobiledv.hr_desktop.screens.salary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.data.model.SalaryCalculationRequest
import uz.mobiledv.hr_desktop.data.model.SalaryRecord
import uz.mobiledv.hr_desktop.screens.SalaryViewModel
import uz.mobiledv.hr_desktop.screens.components.ErrorMessage
import uz.mobiledv.hr_desktop.screens.components.LoadingIndicator
import uz.mobiledv.hr_desktop.screens.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    viewModel: SalaryViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCalculateDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadSalaryRecords()
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
                    text = "Salary Management",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage employee salaries and payments",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { showCalculateDialog = true },
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Calculate Salary")
            }
        }

        Spacer(Modifier.height(24.dp))

        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search by employee ID or payment status...",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Content
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
                    onRetry = { viewModel.loadSalaryRecords() }
                )
            }
            else -> {
                // Salary Records List
                val filteredRecords = uiState.salaryRecords.filter { record ->
                    searchQuery.isBlank() ||
                            record.employeeId.contains(searchQuery, ignoreCase = true) ||
                            record.paymentStatus.contains(searchQuery, ignoreCase = true)
                }

                Card(
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Header Row
                        item {
                            SalaryHeaderRow()
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }

                        items(filteredRecords) { record ->
                            SalaryRecordRow(
                                record = record,
                                onUpdateStatus = { id, status ->
                                    viewModel.updatePaymentStatus(id, status)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Calculate Salary Dialog
    if (showCalculateDialog) {
        CalculateSalaryDialog(
            onDismiss = { showCalculateDialog = false },
            onConfirm = { request ->
                viewModel.calculateSalary(request)
                showCalculateDialog = false
            }
        )
    }
}

@Composable
fun SalaryHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Employee ID",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Period",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Base Amount",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Net Amount",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Status",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Actions",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryRecordRow(
    record: SalaryRecord,
    onUpdateStatus: (String, String) -> Unit
) {
    var showStatusDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = record.employeeId,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${record.periodStart} to ${record.periodEnd}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${String.format("%.2f", record.baseAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${String.format("%.2f", record.netAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Status Badge
            Surface(
                color = when (record.paymentStatus) {
                    "PAID" -> Color.Green.copy(alpha = 0.1f)
                    "PENDING" -> Color.Magenta.copy(alpha = 0.1f)
                    "FAILED" -> Color.Red.copy(alpha = 0.1f)
                    else -> Color.Gray.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = record.paymentStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (record.paymentStatus) {
                        "PAID" -> Color.Green
                        "PENDING" -> Color.Magenta
                        "FAILED" -> Color.Red
                        else -> Color.Gray
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.width(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { showStatusDialog = true }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Update Status",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    // Status Update Dialog
    if (showStatusDialog) {
        UpdateStatusDialog(
            currentStatus = record.paymentStatus,
            onDismiss = { showStatusDialog = false },
            onConfirm = { newStatus ->
                onUpdateStatus(record.id, newStatus)
                showStatusDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateSalaryDialog(
    onDismiss: () -> Unit,
    onConfirm: (SalaryCalculationRequest) -> Unit
) {
    var employeeId by remember { mutableStateOf("") }
    var periodStart by remember { mutableStateOf("") }
    var periodEnd by remember { mutableStateOf("") }
    var bonus by remember { mutableStateOf("0.0") }
    var deductions by remember { mutableStateOf("0.0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.7f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Calculate Salary",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // Form Fields
                OutlinedTextField(
                    value = employeeId,
                    onValueChange = { employeeId = it },
                    label = { Text("Employee ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = periodStart,
                        onValueChange = { periodStart = it },
                        label = { Text("Period Start (YYYY-MM-DD)") },
                        placeholder = { Text("2025-01-01") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = periodEnd,
                        onValueChange = { periodEnd = it },
                        label = { Text("Period End (YYYY-MM-DD)") },
                        placeholder = { Text("2025-01-31") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = bonus,
                        onValueChange = { bonus = it },
                        label = { Text("Bonus Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = deductions,
                        onValueChange = { deductions = it },
                        label = { Text("Deductions") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.weight(1f))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (employeeId.isNotBlank() && periodStart.isNotBlank() && periodEnd.isNotBlank()) {
                                val request = SalaryCalculationRequest(
                                    employeeId = employeeId,
                                    periodStart = periodStart,
                                    periodEnd = periodEnd,
                                    bonus = bonus.toDoubleOrNull() ?: 0.0,
                                    deductions = deductions.toDoubleOrNull() ?: 0.0
                                )
                                onConfirm(request)
                            }
                        },
                        enabled = employeeId.isNotBlank() && periodStart.isNotBlank() && periodEnd.isNotBlank()
                    ) {
                        Text("Calculate")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateStatusDialog(
    currentStatus: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(currentStatus) }
    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("PENDING", "PAID", "FAILED", "PROCESSING")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.width(400.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Update Payment Status",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Payment Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(selectedStatus) },
                        enabled = selectedStatus != currentStatus
                    ) {
                        Text("Update")
                    }
                }
            }
        }
    }
}