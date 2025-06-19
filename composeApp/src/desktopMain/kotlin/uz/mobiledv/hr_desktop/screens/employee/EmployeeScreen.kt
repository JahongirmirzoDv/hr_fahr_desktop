package uz.mobiledv.hr_desktop.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import org.koin.compose.koinInject
import uz.mobiledv.hr_desktop.data.model.*
import uz.mobiledv.hr_desktop.screens.components.ErrorMessage
import uz.mobiledv.hr_desktop.screens.components.LoadingIndicator
import uz.mobiledv.hr_desktop.screens.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeScreen(
    viewModel: EmployeeViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedEmployee by remember { mutableStateOf<Employee?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var departmentFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        viewModel.loadEmployees()
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
                    text = "Employee Management",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage employee information and records",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.height(40.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Employee")
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
                placeholder = "Search employees...",
                modifier = Modifier.weight(1f)
            )

            // Department Filter
            var departmentExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = departmentExpanded,
                onExpandedChange = { departmentExpanded = it },
                modifier = Modifier.width(200.dp)
            ) {
                OutlinedTextField(
                    value = departmentFilter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Department") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = departmentExpanded,
                    onDismissRequest = { departmentExpanded = false }
                ) {
                    val departments = listOf("All", "Engineering", "HR", "Finance", "Marketing", "Sales")
                    departments.forEach { department ->
                        DropdownMenuItem(
                            text = { Text(department) },
                            onClick = {
                                departmentFilter = department
                                departmentExpanded = false
                            }
                        )
                    }
                }
            }
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
                    onRetry = { viewModel.loadEmployees() }
                )
            }
            else -> {
                // Employee List
                val filteredEmployees = uiState.employees.filter { employee ->
                    (searchQuery.isBlank() || employee.name.contains(searchQuery, ignoreCase = true) ||
                            employee.email.contains(searchQuery, ignoreCase = true) ||
                            employee.position.contains(searchQuery, ignoreCase = true)) &&
                            (departmentFilter == "All" || employee.department == departmentFilter)
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
                            EmployeeHeaderRow()
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }

                        items(filteredEmployees) { employee ->
                            EmployeeRow(
                                employee = employee,
                                onEdit = {
                                    selectedEmployee = employee
                                    showEditDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteEmployee(employee.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Employee Dialog
    if (showAddDialog) {
        AddEmployeeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { employee, photo ->
                viewModel.createEmployee(employee, photo)
                showAddDialog = false
            }
        )
    }

    // Edit Employee Dialog
    if (showEditDialog && selectedEmployee != null) {
        EditEmployeeDialog(
            employee = selectedEmployee!!,
            onDismiss = {
                showEditDialog = false
                selectedEmployee = null
            },
            onConfirm = { employee ->
                viewModel.updateEmployee(selectedEmployee!!.id, employee)
                showEditDialog = false
                selectedEmployee = null
            }
        )
    }
}

@Composable
fun EmployeeHeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Name",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = "Email",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text = "Position",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Department",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Salary",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = "Status",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.8f)
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
fun EmployeeRow(
    employee: Employee,
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
                text = employee.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1.5f)
            )
            Text(
                text = employee.email,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1.5f)
            )
            Text(
                text = employee.position,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = employee.department,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$${String.format("%.2f", employee.salaryAmount)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.8f)
            )

            // Status Badge
            Surface(
                color = if (employee.isActive) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    text = if (employee.isActive) "Active" else "Inactive",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (employee.isActive) Color.Green else Color.Red,
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
fun AddEmployeeDialog(
    onDismiss: () -> Unit,
    onConfirm: (EmployeeCreateRequest, ByteArray) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var salaryType by remember { mutableStateOf(SalaryType.MONTHLY) }
    var salaryAmount by remember { mutableStateOf("") }
    var salaryRate by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var selectedPhoto by remember { mutableStateOf<ByteArray?>(null) }
    var showFilePicker by remember { mutableStateOf(false) }

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
                    text = "Add New Employee",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // Form Fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Position") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        label = { Text("Department") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Salary Information
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Salary Type Dropdown
                    var salaryTypeExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = salaryTypeExpanded,
                        onExpandedChange = { salaryTypeExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = salaryType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Salary Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = salaryTypeExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = salaryTypeExpanded,
                            onDismissRequest = { salaryTypeExpanded = false }
                        ) {
                            SalaryType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        salaryType = type
                                        salaryTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = salaryAmount,
                        onValueChange = { salaryAmount = it },
                        label = { Text("Salary Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = salaryRate,
                    onValueChange = { salaryRate = it },
                    label = { Text("Salary Rate (Optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Photo Upload
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Employee Photo:")
                    Button(onClick = { showFilePicker = true }) {
                        Icon(Icons.Default.Upload, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (selectedPhoto != null) "Photo Selected" else "Select Photo")
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Active Status
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                    Text("Active Employee")
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
                            if (name.isNotBlank() && email.isNotBlank() && position.isNotBlank() &&
                                department.isNotBlank() && salaryAmount.isNotBlank() && selectedPhoto != null) {
                                val employee = EmployeeCreateRequest(
                                    userId = "", // This should be handled by the backend
                                    name = name,
                                    position = position,
                                    department = department,
                                    salaryType = salaryType,
                                    salaryAmount = salaryAmount.toDouble(),
                                    salaryRate = salaryRate.toDoubleOrNull(),
                                    isActive = isActive
                                )
                                onConfirm(employee, selectedPhoto!!)
                            }
                        },
                        enabled = name.isNotBlank() && email.isNotBlank() && position.isNotBlank() &&
                                department.isNotBlank() && salaryAmount.isNotBlank() && selectedPhoto != null
                    ) {
                        Text("Add Employee")
                    }
                }
            }
        }
    }

    FilePicker(show = showFilePicker, fileExtensions = listOf("jpg", "jpeg", "png")) { file ->
        showFilePicker = false
        file?.let {
            selectedPhoto = java.io.File(it.path).readBytes()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmployeeDialog(
    employee: Employee,
    onDismiss: () -> Unit,
    onConfirm: (EmployeeCreateRequest) -> Unit
) {
    var name by remember { mutableStateOf(employee.name) }
    var position by remember { mutableStateOf(employee.position) }
    var department by remember { mutableStateOf(employee.department) }
    var salaryType by remember { mutableStateOf(employee.salaryType) }
    var salaryAmount by remember { mutableStateOf(employee.salaryAmount.toString()) }
    var salaryRate by remember { mutableStateOf(employee.salaryRate?.toString() ?: "") }
    var isActive by remember { mutableStateOf(employee.isActive) }

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
                    text = "Edit Employee",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(16.dp))

                // Form Fields (similar to Add Employee but pre-filled)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = position,
                        onValueChange = { position = it },
                        label = { Text("Position") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        label = { Text("Department") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var salaryTypeExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = salaryTypeExpanded,
                        onExpandedChange = { salaryTypeExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = salaryType.name,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Salary Type") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = salaryTypeExpanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = salaryTypeExpanded,
                            onDismissRequest = { salaryTypeExpanded = false }
                        ) {
                            SalaryType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = {
                                        salaryType = type
                                        salaryTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = salaryAmount,
                        onValueChange = { salaryAmount = it },
                        label = { Text("Salary Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = salaryRate,
                    onValueChange = { salaryRate = it },
                    label = { Text("Salary Rate (Optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                    Text("Active Employee")
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
                            if (name.isNotBlank() && position.isNotBlank() &&
                                department.isNotBlank() && salaryAmount.isNotBlank()) {
                                val updatedEmployee = EmployeeCreateRequest(
                                    userId = employee.userId,
                                    name = name,
                                    position = position,
                                    department = department,
                                    salaryType = salaryType,
                                    salaryAmount = salaryAmount.toDouble(),
                                    salaryRate = salaryRate.toDoubleOrNull(),
                                    isActive = isActive
                                )
                                onConfirm(updatedEmployee)
                            }
                        },
                        enabled = name.isNotBlank() && position.isNotBlank() &&
                                department.isNotBlank() && salaryAmount.isNotBlank()
                    ) {
                        Text("Update Employee")
                    }
                }
            }
        }
    }
}