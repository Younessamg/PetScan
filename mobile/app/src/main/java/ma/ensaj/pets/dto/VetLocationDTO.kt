package ma.ensaj.pets.dto

data class VetLocationDTO(
    val id: Long? = null,
    val fullName: String,
    val clinicAddress: String,
    val phoneNumber: String?,
    val latitude: Double,
    val longitude: Double,
    val distance: Double?, // Distance in kilometers (optional)
    val workingHours: String?,
    val emergencyService: Boolean
)
