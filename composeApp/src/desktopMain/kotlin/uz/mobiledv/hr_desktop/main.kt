package uz.mobiledv.hr_desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext.startKoin
import uz.mobiledv.hr_desktop.di.appModule

fun main() = application {
    startKoin {
        modules(appModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "hr_desktop_!",
    ) {
        App()
    }
}