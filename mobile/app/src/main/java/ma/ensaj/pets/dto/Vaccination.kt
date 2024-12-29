package ma.ensaj.pets.dto

data class Vaccination(
    val id: Long? = null,
    val name: String,
    val description: String,
    val dateAdministered: String,
    val nextDueDate: String? = null,
    val veterinarian: String? = null,
    val notes: String? = null
)
