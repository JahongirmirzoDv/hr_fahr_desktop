package uz.mobiledv.hr_desktop.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

class KtorUserService(
    private val client: HttpClient,
    private val baseUrl: String
) : UserService {


    override suspend fun registerUser(name: String, email: String): Boolean {
        val response: HttpResponse = client.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name, "email" to email))
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun sendFaceData(userId: String, encoding: List<Float>): Boolean {
        val response = client.post("$baseUrl/face/save") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("userId" to userId, "encoding" to encoding))
        }
        return response.status == HttpStatusCode.OK
    }
}
