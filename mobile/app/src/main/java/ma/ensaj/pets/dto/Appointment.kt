package ma.ensaj.pets.dto

import org.threeten.bp.LocalDateTime

data class Appointment(
    val id: Long? = null,
    val pet: Pet,  // Changé de petId à pet
    val veterinarian: Veterinarian,  // Changé de veterinarianId à veterinarian
    val appointmentDateTime: LocalDateTime,
    val reason: String? = null,
    val status: AppointmentStatus = AppointmentStatus.SCHEDULED,
    val notes: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
