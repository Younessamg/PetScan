package ma.ensa.pet.repository;

import ma.ensa.pet.model.Appointment;
import ma.ensa.pet.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPetId(Long petId);
    List<Appointment> findByVeterinarianId(Long veterinarianId);
    List<Appointment> findByPetIdAndStatus(Long petId, AppointmentStatus status);
    List<Appointment> findByVeterinarianIdAndAppointmentDateTimeBetween(
            Long veterinarianId, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPetIdAndAppointmentDateTimeAfter(Long petId, LocalDateTime date);
}