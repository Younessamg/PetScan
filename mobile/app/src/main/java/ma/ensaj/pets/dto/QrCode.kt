package ma.ensaj.pets.dto


data class QrCode(
    val id: Long? = null,
    val code: String,
    val imagePath: String,
    val generatedAt: String? = null,
    val scanHistory: List<ScanHistory> = emptyList()
)
