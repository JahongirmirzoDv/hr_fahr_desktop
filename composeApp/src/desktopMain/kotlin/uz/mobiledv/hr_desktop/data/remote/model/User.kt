package uz.mobiledv.hr_desktop.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val fullName: String,
    val id: String,
    val role: String
)