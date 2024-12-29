package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Appointment;
import ma.ensa.pet.model.AppointmentStatus;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.model.Veterinarian;
import ma.ensa.pet.repository.AppointmentRepository;
import ma.ensa.pet.repository.PetRepository;
import ma.ensa.pet.repository.VeterinarianRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final VeterinarianRepository veterinarianRepository;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Pet pet = petRepository.findById(appointment.getPet().getId())
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        Veterinarian vet = veterinarianRepository.findById(appointment.getVeterinarian().getId())
                .orElseThrow(() -> new RuntimeException("Veterinarian not found"));

        appointment.setPet(pet);
        appointment.setVeterinarian(vet);

        return ResponseEntity.ok(appointmentRepository.save(appointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.getPet().getName();
                    appointment.getVeterinarian().getFirstName();
                    return ResponseEntity.ok(appointment);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Appointment>> getPetAppointments(@PathVariable Long petId) {
        if (!petRepository.existsById(petId)) {
            return ResponseEntity.notFound().build();
        }
        List<Appointment> appointments = appointmentRepository.findByPetId(petId);
        // Ensure everything is loaded
        appointments.forEach(appointment -> {
            appointment.getPet().getName(); // Force load
            appointment.getVeterinarian().getFirstName(); // Force load
        });
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/veterinarian/{vetId}")
    public ResponseEntity<List<Appointment>> getVeterinarianAppointments(
            @PathVariable Long vetId,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<Appointment> appointments = appointmentRepository
                .findByVeterinarianIdAndAppointmentDateTimeBetween(vetId, start, end);
        appointments.forEach(appointment -> {
            appointment.getPet().getName(); // Force load
            appointment.getVeterinarian().getFirstName(); // Force load
        });
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/pet/{petId}/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long petId) {
        List<Appointment> appointments = appointmentRepository
                .findByPetIdAndAppointmentDateTimeAfter(petId, LocalDateTime.now());
        appointments.forEach(appointment -> {
            appointment.getPet().getName(); // Force load
            appointment.getVeterinarian().getFirstName(); // Force load
        });
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointmentDetails) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setAppointmentDateTime(appointmentDetails.getAppointmentDateTime());
                    appointment.setReason(appointmentDetails.getReason());
                    appointment.setStatus(appointmentDetails.getStatus());
                    appointment.setNotes(appointmentDetails.getNotes());
                    Appointment updated = appointmentRepository.save(appointment);
                    // Force load relationships
                    updated.getPet().getName();
                    updated.getVeterinarian().getFirstName();
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setStatus(status);
                    Appointment updated = appointmentRepository.save(appointment);
                    // Force load relationships
                    updated.getPet().getName();
                    updated.getVeterinarian().getFirstName();
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointmentRepository.delete(appointment);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}