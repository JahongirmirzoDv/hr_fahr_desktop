package uz.mobiledv.hr_desktop.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import uz.mobiledv.hr_desktop.data.remote.model.AuthDto
import uz.mobiledv.hr_desktop.data.remote.model.LoginRequest

class KtorAuthService(
    private val client: HttpClient,
    private val baseUrl: String
): AuthService {
    override suspend fun login(loginRequest: LoginRequest): AuthDto {
        val response: HttpResponse = client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(loginRequest)
        }
        val authData: AuthDto = response.body()
        return authData
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: String
    ) {

    }
}