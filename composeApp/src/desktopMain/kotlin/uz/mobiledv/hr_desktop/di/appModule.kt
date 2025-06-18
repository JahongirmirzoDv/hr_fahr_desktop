package uz.mobiledv.hr_desktop.di

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
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
import kotlin.math.sin

const val BASE_URL = "http://localhost:8080"
val appModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            encodeDefaults = true // Add this line
        }
    }

    singleOf<Settings>(::Settings)

    single<UserService> { KtorUserService(get(), BASE_URL) }
    single<EmployeeService> { KtorEmployeeService(get(), BASE_URL) }
    single<AuthService> { KtorAuthService(get(), BASE_URL) }
    single<AuthSettings> { AuthSettingsImpl(get(), get()) }

    singleOf(::AuthRepository)

    viewModel { UserViewModel(get()) }
    viewModel { AuthViewModel(get(), get()) }
    viewModel { DashboardViewModel() }
    viewModel { EmployeeViewModel() }
    viewModel { AttendanceViewModel() }
    viewModel { ReportViewModel() }


    single {
        HttpClient(CIO) { // You can choose other engines like OkHttp or Java
            // Logging for HTTP requests and responses
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL // Log everything, adjust as needed for production
            }
            // Content negotiation for JSON serialization/deserialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Important for API evolution
                })
            }
            // Default request configuration (e.g., base URL, headers) can be set here
            install(DefaultRequest) {
                url(BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
}