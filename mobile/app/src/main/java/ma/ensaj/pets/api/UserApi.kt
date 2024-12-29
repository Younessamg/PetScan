package ma.ensaj.pets.api

import ma.ensaj.pets.dto.User
import retrofit2.Call
import retrofit2.http.*

interface UserApi {

    // Créer un utilisateur
    @POST("users")
    fun createUser(
        @Body user: User
    ): Call<User>

    // Mettre à jour un utilisateur
    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: Long,
        @Body user: User
    ): Call<User>

    // Supprimer un utilisateur
    @DELETE("users/{id}")
    fun deleteUser(
        @Path("id") id: Long
    ): Call<Void>

    // Récupérer un utilisateur par son ID
    @GET("users/{id}")
    fun getUserById(
        @Path("id") id: Long
    ): Call<User>

    // Récupérer un utilisateur par son email
    @GET("users/email/{email}")
    fun getUserByEmail(
        @Path("email") email: String
    ): Call<User>

    // Récupérer tous les utilisateurs
    @GET("users")
    fun getAllUsers(): Call<List<User>>
}
