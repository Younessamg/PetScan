package ma.ensaj.pets.api

import ma.ensaj.pets.dto.AuthResponse
import ma.ensaj.pets.dto.LoginRequest
import ma.ensaj.pets.dto.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/register")
    fun register(@Body user: User): Call<User>

    @POST("users/login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>
}
