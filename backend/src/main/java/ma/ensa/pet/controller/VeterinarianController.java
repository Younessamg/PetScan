package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.dto.VetLocationDTO;
import ma.ensa.pet.model.Veterinarian;
import ma.ensa.pet.repository.VeterinarianRepository;
import ma.ensa.pet.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/veterinarians")
@RequiredArgsConstructor
public class VeterinarianController {
    private final VeterinarianRepository veterinarianRepository;
    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<?> createVeterinarian(@RequestBody Veterinarian veterinarian) {
        try {
            System.out.println("Received veterinarian data: " + veterinarian); // Debug log

            // Basic validation
            if (veterinarian.getFirstName() == null || veterinarian.getFirstName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("First name is required");
            }
            if (veterinarian.getLastName() == null || veterinarian.getLastName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Last name is required");
            }
            if (veterinarian.getEmail() == null || veterinarian.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (veterinarian.getLatitude() == null || veterinarian.getLongitude() == null) {
                return ResponseEntity.badRequest().body("Latitude and longitude are required");
            }

            if (veterinarianRepository.findByEmail(veterinarian.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("Email already exists");
            }

            Veterinarian savedVet = veterinarianRepository.save(veterinarian);
            return ResponseEntity.ok(savedVet);
        } catch (Exception e) {
            e.printStackTrace(); // Debug log
            return ResponseEntity.badRequest().body("Error creating veterinarian: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Veterinarian controller is working!");
    }


    @GetMapping("/nearby")
    public ResponseEntity<List<VetLocationDTO>> getNearbyVeterinarians(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radius) {
        List<Veterinarian> nearbyVets = veterinarianRepository.findNearbyVeterinarians(latitude, longitude, radius);
        List<VetLocationDTO> dtos = nearbyVets.stream()
                .map(vet -> locationService.convertToLocationDTO(vet, latitude, longitude))
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/emergency")
    public ResponseEntity<List<VetLocationDTO>> getNearbyEmergencyVeterinarians(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10.0") double radius) {
        List<Veterinarian> emergencyVets = veterinarianRepository.findNearbyEmergencyVeterinarians(latitude, longitude, radius);
        List<VetLocationDTO> dtos = emergencyVets.stream()
                .map(vet -> locationService.convertToLocationDTO(vet, latitude, longitude))
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Veterinarian>> getVeterinariansByCity(@PathVariable String city) {
        return ResponseEntity.ok(veterinarianRepository.findByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinarian> getVeterinarian(@PathVariable Long id) {
        return veterinarianRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinarian> updateVeterinarian(
            @PathVariable Long id,
            @RequestBody Veterinarian veterinarianDetails) {
        return veterinarianRepository.findById(id)
                .map(veterinarian -> {
                    veterinarian.setFirstName(veterinarianDetails.getFirstName());
                    veterinarian.setLastName(veterinarianDetails.getLastName());
                    veterinarian.setPhoneNumber(veterinarianDetails.getPhoneNumber());
                    veterinarian.setSpecialization(veterinarianDetails.getSpecialization());
                    veterinarian.setClinicAddress(veterinarianDetails.getClinicAddress());
                    veterinarian.setLatitude(veterinarianDetails.getLatitude());
                    veterinarian.setLongitude(veterinarianDetails.getLongitude());
                    veterinarian.setCity(veterinarianDetails.getCity());
                    veterinarian.setState(veterinarianDetails.getState());
                    veterinarian.setCountry(veterinarianDetails.getCountry());
                    veterinarian.setPostalCode(veterinarianDetails.getPostalCode());
                    veterinarian.setWorkingHours(veterinarianDetails.getWorkingHours());
                    veterinarian.setEmergencyService(veterinarianDetails.isEmergencyService());
                    veterinarian.setDescription(veterinarianDetails.getDescription());
                    return ResponseEntity.ok(veterinarianRepository.save(veterinarian));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable Long id) {
        return veterinarianRepository.findById(id)
                .map(veterinarian -> {
                    veterinarianRepository.delete(veterinarian);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}