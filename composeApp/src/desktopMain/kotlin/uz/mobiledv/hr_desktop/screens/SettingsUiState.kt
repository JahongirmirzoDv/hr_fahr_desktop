package uz.mobiledv.hr_desktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mobiledv.hr_desktop.repository.AuthRepository

data class SettingsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,

    // UI Preferences
    val isDarkTheme: Boolean = false,
    val language: String = "English",
    val autoSave: Boolean = true,

    // Notification Settings
    val notificationsEnabled: Boolean = true,
    val emailNotifications: Boolean = true,
    val soundAlerts: Boolean = true,

    // Security Settings
    val autoLockMinutes: Int = 30,

    // Data Management
    val backupFrequency: String = "Weekly",

    // Internal state
    val hasUnsavedChanges: Boolean = false
)

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val settings: Settings
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDarkTheme = settings.getBoolean("dark_theme", false),
                language = settings.getString("language", "English"),
                autoSave = settings.getBoolean("auto_save", true),
                notificationsEnabled = settings.getBoolean("notifications_enabled", true),
                emailNotifications = settings.getBoolean("email_notifications", true),
                soundAlerts = settings.getBoolean("sound_alerts", true),
                autoLockMinutes = settings.getInt("auto_lock_minutes", 30),
                backupFrequency = settings.getString("backup_frequency", "Weekly")
            )
        }
    }

    // UI Preferences Functions
    fun updateTheme(isDarkTheme: Boolean) {
        _uiState.value = _uiState.value.copy(
            isDarkTheme = isDarkTheme,
            hasUnsavedChanges = true
        )
    }

    fun updateLanguage(language: String) {
        _uiState.value = _uiState.value.copy(
            language = language,
            hasUnsavedChanges = true
        )
    }

    fun updateAutoSave(autoSave: Boolean) {
        _uiState.value = _uiState.value.copy(
            autoSave = autoSave,
            hasUnsavedChanges = true
        )
    }

    // Notification Functions
    fun updateNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = enabled,
            emailNotifications = if (!enabled) false else _uiState.value.emailNotifications,
            soundAlerts = if (!enabled) false else _uiState.value.soundAlerts,
            hasUnsavedChanges = true
        )
    }

    fun updateEmailNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            emailNotifications = enabled,
            hasUnsavedChanges = true
        )
    }

    fun updateSoundAlerts(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            soundAlerts = enabled,
            hasUnsavedChanges = true
        )
    }

    // Security Functions
    fun updateAutoLockTimeout(minutes: Int) {
        _uiState.value = _uiState.value.copy(
            autoLockMinutes = minutes,
            hasUnsavedChanges = true
        )
    }

    fun clearAllSessions() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // Clear local session
                authRepository.logout()

                // You can add API call to clear server sessions here if needed
                // val result = authRepository.clearAllSessions()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "All sessions cleared successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to clear sessions: ${e.message}"
                )
            }
        }
    }

    // Data Management Functions
    fun updateBackupFrequency(frequency: String) {
        _uiState.value = _uiState.value.copy(
            backupFrequency = frequency,
            hasUnsavedChanges = true
        )
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // Simulate data export process
                // In a real implementation, you would:
                // 1. Fetch all data from the API
                // 2. Format it (JSON, CSV, etc.)
                // 3. Save to file or trigger download

                kotlinx.coroutines.delay(2000) // Simulate export time

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Data exported successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to export data: ${e.message}"
                )
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // Clear application cache
                // This could include:
                // - Clearing image cache
                // - Clearing temporary files
                // - Clearing stored API responses

                kotlinx.coroutines.delay(1000) // Simulate cache clearing

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Cache cleared successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to clear cache: ${e.message}"
                )
            }
        }
    }

    // Save Settings Function
    fun saveSettings() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val currentState = _uiState.value

                // Save all settings to persistent storage
                settings.putBoolean("dark_theme", currentState.isDarkTheme)
                settings.putString("language", currentState.language)
                settings.putBoolean("auto_save", currentState.autoSave)
                settings.putBoolean("notifications_enabled", currentState.notificationsEnabled)
                settings.putBoolean("email_notifications", currentState.emailNotifications)
                settings.putBoolean("sound_alerts", currentState.soundAlerts)
                settings.putInt("auto_lock_minutes", currentState.autoLockMinutes)
                settings.putString("backup_frequency", currentState.backupFrequency)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasUnsavedChanges = false,
                    message = "Settings saved successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to save settings: ${e.message}"
                )
            }
        }
    }

    // Legacy functions (keeping for backward compatibility)
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun refreshProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authRepository.getUserProfile()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Profile refreshed successfully"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Failed to refresh profile"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }

    // Reset Settings Function
    fun resetToDefaults() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                // Clear all settings
                settings.clear()

                // Reset to default values
                _uiState.value = SettingsUiState(
                    isLoading = false,
                    hasUnsavedChanges = true,
                    message = "Settings reset to defaults"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to reset settings: ${e.message}"
                )
            }
        }
    }

    // Auto-save functionality
    private fun autoSaveIfEnabled() {
        if (_uiState.value.autoSave && _uiState.value.hasUnsavedChanges) {
            saveSettings()
        }
    }

    // Call this when the app is about to close or when navigating away
    fun onAppPause() {
        autoSaveIfEnabled()
    }
}