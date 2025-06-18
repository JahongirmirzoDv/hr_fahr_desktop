package uz.mobiledv.hr_desktop.data.remote.repository

interface UserService {
    suspend fun registerUser(name: String, email: String): Boolean
    suspend fun sendFaceData(userId: String, encoding: List<Float>): Boolean
}
