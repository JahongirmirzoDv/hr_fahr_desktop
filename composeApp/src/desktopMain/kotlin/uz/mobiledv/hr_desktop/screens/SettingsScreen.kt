@file:OptIn(ExperimentalMaterial3Api::class)

package uz.mobiledv.hr_desktop.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Configure your HR management system",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(Modifier.height(32.dp))
        
        // General Settings Section
        SettingsSection(
            title = "General Settings",
            icon = Icons.Default.Settings
        ) {
            SettingsItem(
                title = "Application Theme",
                subtitle = "Choose between light and dark theme",
                icon = Icons.Default.Palette,
                action = {
                    Switch(
                        checked = uiState.isDarkTheme,
                        onCheckedChange = { viewModel.updateTheme(it) }
                    )
                }
            )
            
            SettingsItem(
                title = "Language",
                subtitle = "Select application language",
                icon = Icons.Default.Language,
                action = {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = uiState.language,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.width(150.dp).menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("English", "Uzbek", "Russian").forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language) },
                                    onClick = {
                                        viewModel.updateLanguage(language)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
            
            SettingsItem(
                title = "Auto-save",
                subtitle = "Automatically save changes",
                icon = Icons.Default.Save,
                action = {
                    Switch(
                        checked = uiState.autoSave,
                        onCheckedChange = { viewModel.updateAutoSave(it) }
                    )
                }
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Notification Settings Section
        SettingsSection(
            title = "Notifications",
            icon = Icons.Default.Notifications
        ) {
            SettingsItem(
                title = "Enable Notifications",
                subtitle = "Receive system notifications",
                icon = Icons.Default.NotificationsActive,
                action = {
                    Switch(
                        checked = uiState.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                }
            )
            
            SettingsItem(
                title = "Email Notifications",
                subtitle = "Receive notifications via email",
                icon = Icons.Default.Email,
                action = {
                    Switch(
                        checked = uiState.emailNotifications,
                        onCheckedChange = { viewModel.updateEmailNotifications(it) },
                        enabled = uiState.notificationsEnabled
                    )
                }
            )
            
            SettingsItem(
                title = "Sound Alerts",
                subtitle = "Play sound for important notifications",
                icon = Icons.Default.VolumeUp,
                action = {
                    Switch(
                        checked = uiState.soundAlerts,
                        onCheckedChange = { viewModel.updateSoundAlerts(it) },
                        enabled = uiState.notificationsEnabled
                    )
                }
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Security Settings Section
        SettingsSection(
            title = "Security",
            icon = Icons.Default.Security
        ) {
            SettingsItem(
                title = "Auto-lock Timeout",
                subtitle = "Automatically lock after inactivity",
                icon = Icons.Default.Lock,
                action = {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = "${uiState.autoLockMinutes} minutes",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.width(150.dp).menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf(5, 10, 15, 30, 60).forEach { minutes ->
                                DropdownMenuItem(
                                    text = { Text("$minutes minutes") },
                                    onClick = {
                                        viewModel.updateAutoLockTimeout(minutes)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
            
            SettingsItem(
                title = "Session Management",
                subtitle = "Manage active sessions",
                icon = Icons.Default.AdminPanelSettings,
                action = {
                    Button(
                        onClick = { viewModel.clearAllSessions() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Clear Sessions")
                    }
                }
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Data Management Section
        SettingsSection(
            title = "Data Management",
            icon = Icons.Default.Storage
        ) {
            SettingsItem(
                title = "Backup Frequency",
                subtitle = "How often to backup data",
                icon = Icons.Default.Backup,
                action = {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = uiState.backupFrequency,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.width(150.dp).menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Daily", "Weekly", "Monthly", "Never").forEach { frequency ->
                                DropdownMenuItem(
                                    text = { Text(frequency) },
                                    onClick = {
                                        viewModel.updateBackupFrequency(frequency)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
            
            SettingsItem(
                title = "Export Data",
                subtitle = "Export all data to external file",
                icon = Icons.Default.Download,
                action = {
                    Button(
                        onClick = { viewModel.exportData() }
                    ) {
                        Text("Export")
                    }
                }
            )
            
            SettingsItem(
                title = "Clear Cache",
                subtitle = "Clear application cache",
                icon = Icons.Default.ClearAll,
                action = {
                    Button(
                        onClick = { viewModel.clearCache() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Clear")
                    }
                }
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        // About Section
        SettingsSection(
            title = "About",
            icon = Icons.Default.Info
        ) {
            SettingsItem(
                title = "Version",
                subtitle = "1.0.0",
                icon = Icons.Default.Info,
                action = null
            )
            
            SettingsItem(
                title = "Developer",
                subtitle = "HR Management Team",
                icon = Icons.Default.Person,
                action = null
            )
            
            SettingsItem(
                title = "License",
                subtitle = "MIT License",
                icon = Icons.Default.Article,
                action = null
            )
        }
        
        Spacer(Modifier.height(32.dp))
        
        // Save Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (uiState.hasUnsavedChanges) {
                Button(
                    onClick = { viewModel.saveSettings() },
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Save Changes")
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    action: @Composable (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        
        if (action != null) {
            action()
        }
    }
}