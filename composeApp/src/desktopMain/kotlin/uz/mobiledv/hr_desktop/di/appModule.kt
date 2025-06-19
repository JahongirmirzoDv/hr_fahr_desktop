package uz.mobiledv.hr_desktop.di

import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.mobiledv.hr_desktop.data.network.ApiService
import uz.mobiledv.hr_desktop.data.network.ApiServiceImpl
import uz.mobiledv.hr_desktop.presentation.viewmodel.*
import uz.mobiledv.hr_desktop.repository.AttendanceRepository
import uz.mobiledv.hr_desktop.repository.AuthRepository
import uz.mobiledv.hr_desktop.repository.DashboardRepository
import uz.mobiledv.hr_desktop.repository.EmployeeRepository
import uz.mobiledv.hr_desktop.repository.ProjectRepository
import uz.mobiledv.hr_desktop.repository.SalaryRepository
import uz.mobiledv.hr_desktop.repository.UserRepository
import uz.mobiledv.hr_desktop.screens.AuthViewModel
import uz.mobiledv.hr_desktop.screens.ProjectViewModel
import uz.mobiledv.hr_desktop.screens.SalaryViewModel
import uz.mobiledv.hr_desktop.screens.UserViewModel
import uz.mobiledv.hr_desktop.screens.attendance.AttendanceViewModel
import uz.mobiledv.hr_desktop.screens.dashboard.DashboardViewModel
import uz.mobiledv.hr_desktop.screens.employee.EmployeeViewModel
import uz.mobiledv.hr_desktop.screens.report.ReportViewModel
import uz.mobiledv.hr_desktop.utils.AuthManager

const val BASE_URL = "http://localhost:8080"

val appModule = module {

    // JSON Configuration
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            encodeDefaults = true
            prettyPrint = true
        }
    }

    // Settings
    single<Settings> { Settings() }

    // Auth Manager
    single { AuthManager(get()) }

    // HTTP Client
    single {
        val json: Json = get()
        val authManager: AuthManager = get()

        HttpClient(CIO) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = authManager.getToken()
                        token?.let {
                            BearerTokens(accessToken = it, refreshToken = "")
                        }
                    }
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            defaultRequest {
                url(BASE_URL)
            }
        }
    }

    // API Service
    single<ApiService> { ApiServiceImpl(get(), BASE_URL) }

    // Repositories
    single { AuthRepository(get(), get()) }
    single { EmployeeRepository(get()) }
    single { UserRepository(get()) }
    single { AttendanceRepository(get()) }
    single { SalaryRepository(get()) }
    single { ProjectRepository(get()) }
    single { DashboardRepository(get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { EmployeeViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { AttendanceViewModel(get()) }
    viewModel { SalaryViewModel(get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { ReportViewModel(get(), get(), get(), get()) }

    // Updated SettingsViewModel with Settings dependency
    viewModel { SettingsViewModel(get(), get()) }
}