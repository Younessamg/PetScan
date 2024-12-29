package ma.ensaj.pets.api

import ma.ensaj.pets.dto.VetLocationDTO
import ma.ensaj.pets.dto.Veterinarian
import retrofit2.Call
import retrofit2.http.*

interface VeterinarianApi {

    // Ajouter un vétérinaire
    @POST("veterinarians")
    fun createVeterinarian(
        @Body veterinarian: Veterinarian
    ): Call<Veterinarian>

    // Tester la disponibilité du contrôleur vétérinaire
    @GET("veterinarians/test")
    fun test(): Call<String>

    // Récupérer les vétérinaires à proximité d'une localisation donnée
    @GET("veterinarians/nearby")
    fun getNearbyVeterinarians(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 5.0
    ): Call<List<VetLocationDTO>>

    // Récupérer les vétérinaires d'urgence à proximité d'une localisation donnée
    @GET("veterinarians/emergency")
    fun getNearbyEmergencyVeterinarians(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Double = 10.0
    ): Call<List<VetLocationDTO>>

    // Récupérer les vétérinaires par ville
    @GET("veterinarians/city/{city}")
    fun getVeterinariansByCity(
        @Path("city") city: String
    ): Call<List<Veterinarian>>

    // Récupérer un vétérinaire par son ID
    @GET("veterinarians/{id}")
    fun getVeterinarian(
        @Path("id") id: Long
    ): Call<Veterinarian>

    // Mettre à jour les informations d'un vétérinaire
    @PUT("veterinarians/{id}")
    fun updateVeterinarian(
        @Path("id") id: Long,
        @Body veterinarianDetails: Veterinarian
    ): Call<Veterinarian>

    // Supprimer un vétérinaire par son ID
    @DELETE("veterinarians/{id}")
    fun deleteVeterinarian(
        @Path("id") id: Long
    ): Call<Void>
}
