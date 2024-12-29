package ma.ensaj.pets.dto

data class Pet(
    val id: Long? = null,
    val name: String,
    val species: String,
    val breed: String,
    val color: String,
    val description: String,
    val registrationDate: String? = null,
    val qrCode: QrCode? = null,
    val medications: List<Medication> = emptyList(),
    val vaccinations: List<Vaccination> = emptyList()
)