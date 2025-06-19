package uz.mobiledv.hr_desktop.data.remote.repository

import uz.mobiledv.hr_desktop.data.remote.model.AuthDto
import uz.mobiledv.hr_desktop.data.remote.model.LoginRequest

interface AuthService {
    suspend fun login(loginRequest: LoginRequest): AuthDto

    suspend fun register(fullName: String, email: String, password: String, role: String)
}