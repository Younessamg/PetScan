package ma.ensaj.pets.dto

data class Veterinarian(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String? = null,
    val specialization: String? = null,
    val clinicAddress: String,
    val latitude: Double,
    val longitude: Double,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val workingHours: String? = null,
    val emergencyService: Boolean,
    val description: String? = null
)
