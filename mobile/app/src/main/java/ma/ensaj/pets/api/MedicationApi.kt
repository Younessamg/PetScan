package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Medication
import retrofit2.Call
import retrofit2.http.*

interface MedicationApi {

    // Ajouter un médicament pour un animal spécifique
    @POST("pets/{petId}/medications")
    fun addMedication(
        @Path("petId") petId: Long,
        @Body medication: Medication
    ): Call<Medication>

    // Récupérer tous les médicaments pour un animal spécifique
    @GET("pets/{petId}/medications")
    fun getMedications(
        @Path("petId") petId: Long
    ): Call<List<Medication>>

    // Récupérer un médicament spécifique par son ID pour un animal spécifique
    @GET("pets/{petId}/medications/{id}")
    fun getMedicationById(
        @Path("petId") petId: Long,
        @Path("id") id: Long
    ): Call<Medication>

    // Mettre à jour un médicament spécifique pour un animal
    @PUT("pets/{petId}/medications/{id}")
    fun updateMedication(
        @Path("petId") petId: Long,
        @Path("id") id: Long,
        @Body medication: Medication
    ): Call<Medication>

    // Supprimer un médicament spécifique pour un animal
    @DELETE("pets/{petId}/medications/{id}")
    fun deleteMedication(
        @Path("petId") petId: Long,
        @Path("id") id: Long
    ): Call<Void>
}
