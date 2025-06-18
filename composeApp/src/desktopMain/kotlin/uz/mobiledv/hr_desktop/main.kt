package uz.mobiledv.hr_desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "hr_desktop_!",
    ) {
        App()
    }
}