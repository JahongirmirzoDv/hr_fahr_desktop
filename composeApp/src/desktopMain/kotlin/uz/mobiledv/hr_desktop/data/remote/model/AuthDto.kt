package uz.mobiledv.hr_desktop.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val token: String,
    val user: User
)