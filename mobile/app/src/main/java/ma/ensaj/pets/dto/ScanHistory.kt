package ma.ensaj.pets.dto

data class ScanHistory(
    val id: Long? = null,
    val scanDateTime: String,
    val location: String? = null,
    val scannerIp: String? = null,
    val additionalInfo: String? = null
)

