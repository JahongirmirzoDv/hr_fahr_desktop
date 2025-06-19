package uz.mobiledv.hr_desktop.screens.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.data.model.Attendance
import uz.mobiledv.hr_desktop.data.model.AttendanceCreateRequest
import uz.mobiledv.hr_desktop.screens.components.ErrorMessage
import uz.mobiledv.hr_desktop.screens.components.LoadingIndicator
import uz.mobiledv.hr_desktop.screens.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedAttendance by remember { mutableStateOf<Attendance?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var employeeFilter by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadAttendanceRecords()
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
                    text = "Attendance Management",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track and manage employee attendance records",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Record")
                }

                Button(
                    onClick = { viewModel.loadAttendanceRecords() },
                    modifier = Modifier.height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Refresh")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Search and Filter Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by employee ID or status...",
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = employeeFilter,
                onValueChange = { employeeFilter = it },
                label = { Text("Employee ID Filter") },
                modifier = Modifier.width(200.dp)
            )
        }

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
                    onRetry = { viewModel.loadAttendanceRecords() }
                )
            }
            else -> {
                // Attendance List
                val filteredAttendance = uiState.attendanceRecords.filter { attendance ->
                    (searchQuery.isBlank() ||
                            attendance.employeeId.contains(searchQuery, ignoreCase = true) ||
                            attendance.status.contains(searchQuery, ignoreCase = true)) &&
                            (employeeFilter.isBlank() || attendance.employeeId == employeeFilter)
                }

                Card(
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Header Row
                        AttendanceHeaderRow()
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Records List
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                       items(filteredAttendance) { attendance ->
                                AttendanceRow(
                                    attendance = attendance,
                                    onEdit = {
                                        selectedAttendance = attendance
                                        showEditDialog = true
                                    },
                                    onDelete = {
                                        viewModel.deleteAttendanceRecord(attendance.id)
                                    }
                                )
                            }
                        }

                        // Pagination
                        if (uiState.totalPages > 1) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { viewModel.previousPage() },
                                    enabled = uiState.hasPreviousPage
                                ) {
                                    Icon(Icons.Default.NavigateBefore, contentDescription = "Previous")
                                }

                                Text(
                                    text = "Page ${uiState.currentPage} of ${uiState.totalPages}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                IconButton(
                                    onClick = { viewModel.nextPage() },
                                    enabled = uiState.hasNextPage
                                ) {
                                    Icon(Icons.Default.NavigateNext, contentDescription = "Next")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Attendance Dialog
    if (showAddDialog) {
        AddAttendanceDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { attendance ->
                viewModel.createAttendanceRecord(attendance)
                showAddDialog = false
            }
        )
    }

    // Edit Attendance Dialog
    if (showEditDialog && selectedAttendance != null) {
        EditAttendanceDialog(
            attendance = selectedAttendance!!,
            onDismiss = {
                showEditDialog = false
                selectedAttendance = null
            },
            onConfirm = { attendance ->
                viewModel.updateAttendanceRecord(selectedAttendance!!.id, attendance)
                showEditDialog = false
                selectedAttendance = null
            }
        )
    }
}

@Composable
fun AttendanceHeaderRow() {
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
            text = "Date",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Check In",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Check Out",
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

@Composable
fun AttendanceRow(
    attendance: Attendance,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                text = attendance.employeeId,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = attendance.date,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = attendance.checkIn ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = attendance.checkOut ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            // Status Badge
            Surface(
                color = when (attendance.status) {
                    "PRESENT" -> Color.Green.copy(alpha = 0.1f)
                    "ABSENT" -> Color.Red.copy(alpha = 0.1f)
                    "LATE" -> Color.Magenta.copy(alpha = 0.1f)
                    else -> Color.Gray.copy(alpha = 0.1f)
                },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = attendance.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (attendance.status) {
                        "PRESENT" -> Color.Green
                        "ABSENT" -> Color.Red
                        "LATE" -> Color.Magenta
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
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAttendanceDialog(
    onDismiss: () -> Unit,
    onConfirm: (AttendanceCreateRequest) -> Unit
) {
    var employeeId by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("PRESENT") }
    var notes by remember { mutableStateOf("") }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("PRESENT", "ABSENT", "LATE", "EARLY_LEAVE", "HALF_DAY")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.8f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Add Attendance Record",
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

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    placeholder = { Text("2024-01-01") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = checkIn,
                        onValueChange = { checkIn = it },
                        label = { Text("Check In (HH:MM)") },
                        placeholder = { Text("09:00") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = checkOut,
                        onValueChange = { checkOut = it },
                        label = { Text("Check Out (HH:MM)") },
                        placeholder = { Text("17:00") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Status Dropdown
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = it }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { statusOption ->
                            DropdownMenuItem(
                                text = { Text(statusOption) },
                                onClick = {
                                    status = statusOption
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

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
                            if (employeeId.isNotBlank() && date.isNotBlank()) {
                                val attendance = AttendanceCreateRequest(
                                    employeeId = employeeId,
                                    date = date,
                                    checkIn = checkIn.ifBlank { null },
                                    checkOut = checkOut.ifBlank { null },
                                    status = status,
                                    notes = notes.ifBlank { null }
                                )
                                onConfirm(attendance)
                            }
                        },
                        enabled = employeeId.isNotBlank() && date.isNotBlank()
                    ) {
                        Text("Add Record")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAttendanceDialog(
    attendance: Attendance,
    onDismiss: () -> Unit,
    onConfirm: (AttendanceCreateRequest) -> Unit
) {
    var employeeId by remember { mutableStateOf(attendance.employeeId) }
    var date by remember { mutableStateOf(attendance.date) }
    var checkIn by remember { mutableStateOf(attendance.checkIn ?: "") }
    var checkOut by remember { mutableStateOf(attendance.checkOut ?: "") }
    var status by remember { mutableStateOf(attendance.status) }
    var notes by remember { mutableStateOf(attendance.notes ?: "") }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("PRESENT", "ABSENT", "LATE", "EARLY_LEAVE", "HALF_DAY")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .fillMaxHeight(0.8f),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Edit Attendance Record",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // Form Fields (similar to Add but pre-filled)
                OutlinedTextField(
                    value = employeeId,
                    onValueChange = { employeeId = it },
                    label = { Text("Employee ID") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false // Don't allow changing employee ID
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = checkIn,
                        onValueChange = { checkIn = it },
                        label = { Text("Check In (HH:MM)") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = checkOut,
                        onValueChange = { checkOut = it },
                        label = { Text("Check Out (HH:MM)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Status Dropdown
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = it }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded)
                        },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { statusOption ->
                            DropdownMenuItem(
                                text = { Text(statusOption) },
                                onClick = {
                                    status = statusOption
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

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
                            if (employeeId.isNotBlank() && date.isNotBlank()) {
                                val updatedAttendance = AttendanceCreateRequest(
                                    employeeId = employeeId,
                                    date = date,
                                    checkIn = checkIn.ifBlank { null },
                                    checkOut = checkOut.ifBlank { null },
                                    status = status,
                                    notes = notes.ifBlank { null }
                                )
                                onConfirm(updatedAttendance)
                            }
                        },
                        enabled = employeeId.isNotBlank() && date.isNotBlank()
                    ) {
                        Text("Update Record")
                    }
                }
            }
        }
    }
}