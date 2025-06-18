package uz.mobiledv.hr_desktop.data.remote.repository

class AuthRepository(private val authService: AuthService) {
    suspend fun login(username: String, password: String) = authService.login(username, password)

    suspend fun register(fullName: String, email: String, password: String, role: String) =
        authService.register(fullName, email, password, role)
}