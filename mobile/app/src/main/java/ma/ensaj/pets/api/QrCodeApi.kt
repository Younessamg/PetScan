package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.dto.QrCode
import retrofit2.Call
import retrofit2.http.*

interface QrCodeApi {

    // Générer un QR code pour un animal spécifique
    @POST("qrcodes/generate/pet/{petId}")
    fun generateQrCode(
        @Path("petId") petId: Long
    ): Call<QrCode>

    // Récupérer un QR code spécifique par son code
    @GET("qrcodes/{code}")
    fun getQrCodeByCode(
        @Path("code") code: String
    ): Call<QrCode>

    // Récupérer l'image du QR code par son code
    @GET("qrcodes/image/{code}")
    fun getQrCodeImage(
        @Path("code") code: String
    ): Call<Void>

    // Supprimer un QR code par son ID
    @DELETE("qrcodes/{id}")
    fun deleteQrCode(
        @Path("id") id: Long
    ): Call<Void>

    // Récupérer tous les QR codes (uniquement pour les admins)
    @GET("qrcodes")
    fun getAllQrCodes(): Call<List<QrCode>>

    // Scanner un QR code et obtenir les informations de l'animal associé
    @GET("qrcodes/scan/{code}")
    fun scanQrCode(
        @Path("code") code: String
    ): Call<Pet>
}
