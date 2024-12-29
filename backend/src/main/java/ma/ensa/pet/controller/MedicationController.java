package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Medication;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.repository.MedicationRepository;
import ma.ensa.pet.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pets/{petId}/medications")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationRepository medicationRepository;
    private final PetRepository petRepository;

    @PostMapping
    public ResponseEntity<Medication> addMedication(@PathVariable Long petId, @RequestBody Medication medication) {
        return petRepository.findById(petId)
                .map(pet -> {
                    medication.setPet(pet);
                    return ResponseEntity.ok(medicationRepository.save(medication));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getMedications(@PathVariable Long petId) {
        if (!petRepository.existsById(petId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicationRepository.findByPetId(petId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedication(@PathVariable Long petId, @PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long petId, @PathVariable Long id,
                                                       @RequestBody Medication medicationDetails) {
        return medicationRepository.findById(id)
                .map(medication -> {
                    medication.setName(medicationDetails.getName());
                    medication.setDescription(medicationDetails.getDescription());
                    medication.setStartDate(medicationDetails.getStartDate());
                    medication.setEndDate(medicationDetails.getEndDate());
                    medication.setDosage(medicationDetails.getDosage());
                    medication.setFrequency(medicationDetails.getFrequency());
                    return ResponseEntity.ok(medicationRepository.save(medication));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long petId, @PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(medication -> {
                    medicationRepository.delete(medication);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}