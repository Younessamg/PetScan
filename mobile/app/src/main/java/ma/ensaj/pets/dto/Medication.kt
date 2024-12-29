package ma.ensaj.pets.dto

data class Medication(
    val id: Long? = null,
    val name: String,
    val description: String? = null,
    val startDate: String,
    val endDate: String? = null,
    val dosage: String? = null,
    val frequency: String? = null
)
