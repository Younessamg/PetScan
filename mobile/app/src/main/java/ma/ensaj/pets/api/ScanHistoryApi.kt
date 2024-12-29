package ma.ensaj.pets.api

import ma.ensaj.pets.dto.ScanHistory
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDateTime

interface ScanHistoryApi {

    // Enregistrer un scan de QR code
    @POST("/scans/record/{qrCode}")
    fun recordScan(
        @Path("qrCode") qrCode: String,
        @Query("location") location: String? = null,
        @Header("User-Agent") userAgent: String
    ): Call<ScanHistory>

    // Récupérer l'historique des scans d'un QR code spécifique
    @GET("/scans/qrcode/{qrCodeId}")
    fun getScanHistoryByQrCodeId(
        @Path("qrCodeId") qrCodeId: Long
    ): Call<List<ScanHistory>>

    // Récupérer l'historique des scans d'un QR code spécifique sur une plage de dates
    @GET("/scans/qrcode/{qrCodeId}/daterange")
    fun getScanHistoryByDateRange(
        @Path("qrCodeId") qrCodeId: Long,
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime
    ): Call<List<ScanHistory>>
}
