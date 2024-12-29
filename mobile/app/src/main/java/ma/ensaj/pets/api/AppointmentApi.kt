package ma.ensaj.pets.api

import ma.ensaj.pets.dto.Appointment
import ma.ensaj.pets.dto.AppointmentStatus
import ma.ensaj.pets.dto.Pet
import ma.ensaj.pets.dto.Veterinarian
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import org.threeten.bp.LocalDateTime

interface AppointmentApi {

    // Créer un rendez-vous
    @POST("appointments")
    fun createAppointment(
        @Body appointment: Appointment
    ): Call<Appointment>

    //Récupérer un rendez-vous par son id
    @GET("appointments/{id}")
    fun getAppointmentById(@Path("id") id: Long): Call<Appointment>

    // Récupérer tous les rendez-vous d'un animal
    @GET("appointments/pet/{petId}")
    fun getPetAppointments(
        @Path("petId") petId: Long
    ): Call<List<Appointment>>

    // Récupérer les rendez-vous d'un vétérinaire dans une plage horaire
    @GET("appointments/veterinarian/{vetId}")
    fun getVeterinarianAppointments(
        @Path("vetId") vetId: Long,
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime
    ): Call<List<Appointment>>

    // Récupérer les rendez-vous à venir d'un animal
    @GET("appointments/pet/{petId}/upcoming")
    fun getUpcomingAppointments(
        @Path("petId") petId: Long
    ): Call<List<Appointment>>

    // Mettre à jour un rendez-vous
    @PUT("appointments/{id}")
    fun updateAppointment(
        @Path("id") id: Long,
        @Body appointmentDetails: Appointment
    ): Call<Appointment>

    // Mettre à jour le statut d'un rendez-vous
    @PUT("appointments/{id}/status")
    fun updateAppointmentStatus(
        @Path("id") id: Long,
        @Query("status") status: AppointmentStatus
    ): Call<Appointment>

    // Annuler un rendez-vous
    @DELETE("appointments/{id}")
    fun cancelAppointment(
        @Path("id") id: Long
    ): Call<Void>
}
