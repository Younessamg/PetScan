package ma.ensaj.pets.dto

data class AuthResponse(
    val message: String,
    val userId: Long,
    val token: String
)

