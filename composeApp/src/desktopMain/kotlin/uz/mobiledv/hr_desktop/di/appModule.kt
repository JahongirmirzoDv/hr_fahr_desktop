package uz.mobiledv.hr_desktop.di

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.mobiledv.hr_desktop.data.remote.api.KtorAuthService
import uz.mobiledv.hr_desktop.data.remote.api.KtorEmployeeService
import uz.mobiledv.hr_desktop.data.remote.api.KtorUserService
import uz.mobiledv.hr_desktop.data.remote.repository.AuthRepository
import uz.mobiledv.hr_desktop.data.remote.repository.AuthService
import uz.mobiledv.hr_desktop.data.remote.repository.EmployeeService
import uz.mobiledv.hr_desktop.data.remote.repository.UserService
import uz.mobiledv.hr_desktop.screens.UserViewModel
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceViewModel
import uz.mobiledv.hr_desktop.screens.auth.AuthViewModel
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardViewModel
import uz.mobiledv.hr_desktop.screens.employee.EmployeeViewModel
import uz.mobiledv.hr_desktop.screens.report.ReportViewModel
import uz.mobiledv.hr_desktop.utils.AuthSettings
import uz.mobiledv.hr_desktop.utils.AuthSettingsImpl

const val BASE_URL = "http://localhost:8080"

val appModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            encodeDefaults = true
        }
    }

    single<Settings> { Settings() }

    single<UserService> { KtorUserService(get(), BASE_URL) }
    single<EmployeeService> { KtorEmployeeService(get(), BASE_URL) }
    single<AuthService> { KtorAuthService(get(), BASE_URL) }
    single<AuthSettings> { AuthSettingsImpl(get(), get()) }

    singleOf(::AuthRepository)

    viewModel { UserViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { DashboardViewModel(get(),get()) }
    viewModel { EmployeeViewModel(get()) }
    viewModel { AttendanceViewModel() }
    viewModel { ReportViewModel() }

    single {
        val json: Json = get()
        val authSettings: AuthSettings = get()

        HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)

                // ✅ Add the bearer token (one-time at creation)
                val token = authSettings.getCurrentUser()?.token
                if (!token.isNullOrBlank()) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}
