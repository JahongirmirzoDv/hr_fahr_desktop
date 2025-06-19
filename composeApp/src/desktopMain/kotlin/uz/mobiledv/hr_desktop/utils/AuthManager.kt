package uz.mobiledv.hr_desktop.utils

import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import uz.mobiledv.hr_desktop.data.model.User

class AuthManager(
    private val settings: Settings
) {
    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "current_user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveAuthData(token: String, user: User) {
        settings.putString(KEY_TOKEN, token)
        settings.putString(KEY_USER, json.encodeToString(user))
        settings.putBoolean(KEY_IS_LOGGED_IN, true)
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_TOKEN)
    }

    fun getCurrentUser(): User? {
        val userJson = settings.getStringOrNull(KEY_USER)
        return userJson?.let {
            try {
                json.decodeFromString<User>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun updateUser(user: User) {
        settings.putString(KEY_USER, json.encodeToString(user))
    }

    fun isLoggedIn(): Boolean {
        return settings.getBoolean(KEY_IS_LOGGED_IN, false) && getToken() != null
    }

    fun clearAuthData() {
        settings.remove(KEY_TOKEN)
        settings.remove(KEY_USER)
        settings.putBoolean(KEY_IS_LOGGED_IN, false)
    }
}