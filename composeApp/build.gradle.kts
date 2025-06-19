import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            // Navigation
            implementation(libs.compose.navigation)

            // Koin for Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.logging)
            implementation(libs.ktor.auth)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.kotlinx.datetime)

            implementation(libs.slf4j.simple)

            implementation(libs.multiplatformSettings.noArg) // Common dependency
            implementation(libs.multiplatformSettings.coroutines) // Optional for coroutines support


            // File picker
            implementation("com.darkrockstudios:mpfilepicker:3.1.0")

            // Charts for reports
            implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

            // Image loading
            implementation("io.coil-kt.coil3:coil-compose:3.2.0")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0")

            // Pagination
            implementation("app.cash.paging:paging-compose-common:3.3.0-alpha02-0.4.0")

            implementation("org.apache.logging.log4j:log4j-api:2.20.0")
            implementation("org.apache.logging.log4j:log4j-core:2.20.0")
            implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0") // If you use SLF4J

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "uz.mobiledv.hr_desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "HR FAHR Desktop"
            packageVersion = "1.0.0"
            description = "Complete HR Management System"
            vendor = "Mobile DV"

            macOS {
                bundleID = "uz.mobiledv.hr_desktop"
                iconFile.set(project.file("src/desktopMain/resources/icon.icns"))
            }

            windows {
                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
                menuGroup = "HR FAHR"
                upgradeUuid = "hr-fahr-desktop"
            }
        }
    }
}
