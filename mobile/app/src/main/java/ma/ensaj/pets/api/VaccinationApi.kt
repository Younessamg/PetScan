package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Vaccination
import retrofit2.Call
import retrofit2.http.*

interface VaccinationApi {

    // Ajouter une vaccination pour un animal spécifique
    @POST("pets/{petId}/vaccinations")
    fun addVaccination(
        @Path("petId") petId: Long,
        @Body vaccination: Vaccination
    ): Call<Vaccination>

    // Récupérer toutes les vaccinations pour un animal spécifique
    @GET("pets/{petId}/vaccinations")
    fun getVaccinations(
        @Path("petId") petId: Long
    ): Call<List<Vaccination>>

    // Récupérer une vaccination spécifique par son ID pour un animal spécifique
    @GET("pets/{petId}/vaccinations/{id}")
    fun getVaccinationById(
        @Path("petId") petId: Long,
        @Path("id") id: Long
    ): Call<Vaccination>

    // Mettre à jour une vaccination spécifique pour un animal
    @PUT("pets/{petId}/vaccinations/{id}")
    fun updateVaccination(
        @Path("petId") petId: Long,
        @Path("id") id: Long,
        @Body vaccination: Vaccination
    ): Call<Vaccination>

    // Supprimer une vaccination spécifique pour un animal
    @DELETE("pets/{petId}/vaccinations/{id}")
    fun deleteVaccination(
        @Path("petId") petId: Long,
        @Path("id") id: Long
    ): Call<Void>
}
