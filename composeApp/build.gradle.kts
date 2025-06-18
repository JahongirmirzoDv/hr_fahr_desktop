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

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.kotlinx.datetime)

            implementation(libs.slf4j.simple)

            implementation(libs.multiplatformSettings.noArg) // Common dependency
            implementation(libs.multiplatformSettings.coroutines) // Optional for coroutines support


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
            packageName = "uz.mobiledv.hr_desktop"
            packageVersion = "1.0.0"
        }
    }
}
