package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Pet
import retrofit2.Call
import retrofit2.http.*

interface PetApi {

    // Ajouter un animal pour un utilisateur spécifique
    @POST("pets/owner/{ownerId}")
    fun createPet(
        @Path("ownerId") ownerId: Long,
        @Body pet: Pet
    ): Call<Pet>

    // Mettre à jour les informations d'un animal
    @PUT("pets/{id}")
    fun updatePet(
        @Path("id") id: Long,
        @Body pet: Pet
    ): Call<Pet>

    // Supprimer un animal par son ID
    @DELETE("pets/{id}")
    fun deletePet(
        @Path("id") id: Long
    ): Call<Void>

    // Récupérer un animal par son ID
    @GET("pets/{id}")
    fun getPetById(
        @Path("id") id: Long
    ): Call<Pet>

    // Récupérer tous les animaux d'un propriétaire par son ID
    @GET("pets/owner/{ownerId}")
    fun getPetsByOwnerId(
        @Path("ownerId") ownerId: Long
    ): Call<List<Pet>>

    // Récupérer tous les animaux (pour les admins)
    @GET("pets")
    fun getAllPets(): Call<List<Pet>>
}
