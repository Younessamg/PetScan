package ma.ensaj.pets.dto

data class User(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)

