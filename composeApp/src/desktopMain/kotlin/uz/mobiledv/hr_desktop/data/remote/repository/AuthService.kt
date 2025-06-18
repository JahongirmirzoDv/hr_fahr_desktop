package uz.mobiledv.hr_desktop.data.remote.repository

import uz.mobiledv.hr_desktop.data.remote.model.AuthDto

interface AuthService {
    suspend fun login(email: String, password: String): AuthDto

    suspend fun register(fullName: String, email: String, password: String, role: String)
}