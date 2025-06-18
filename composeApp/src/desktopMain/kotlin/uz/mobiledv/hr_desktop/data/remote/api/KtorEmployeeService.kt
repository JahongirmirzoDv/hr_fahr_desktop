package uz.mobiledv.hr_desktop.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import uz.mobiledv.hr_desktop.data.remote.model.EmployeeDto
import uz.mobiledv.hr_desktop.data.remote.repository.EmployeeService

class KtorEmployeeService(
    private val client: HttpClient,
    private val baseUrl: String
) : EmployeeService {

    override suspend fun getAll(): List<EmployeeDto> {
        return client.get("$baseUrl/admin/employees").body()
    }

    override suspend fun create(employee: EmployeeDto): Boolean {
        val res = client.post("$baseUrl/admin/employees") {
            contentType(ContentType.Application.Json)
            setBody(employee)
        }
        return res.status == HttpStatusCode.Created
    }

    override suspend fun delete(id: String): Boolean {
        val res = client.delete("$baseUrl/admin/employees/$id")
        return res.status == HttpStatusCode.OK
    }
}
