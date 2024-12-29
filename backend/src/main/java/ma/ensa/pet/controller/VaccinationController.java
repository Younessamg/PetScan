package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Vaccination;
import ma.ensa.pet.repository.VaccinationRepository;
import ma.ensa.pet.repository.PetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pets/{petId}/vaccinations")
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationRepository vaccinationRepository;
    private final PetRepository petRepository;

    @PostMapping
    public ResponseEntity<Vaccination> addVaccination(@PathVariable Long petId, @RequestBody Vaccination vaccination) {
        return petRepository.findById(petId)
                .map(pet -> {
                    vaccination.setPet(pet);
                    return ResponseEntity.ok(vaccinationRepository.save(vaccination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable Long petId) {
        if (!petRepository.existsById(petId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vaccinationRepository.findByPetId(petId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long petId, @PathVariable Long id) {
        return vaccinationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vaccination> updateVaccination(@PathVariable Long petId, @PathVariable Long id,
                                                         @RequestBody Vaccination vaccinationDetails) {
        return vaccinationRepository.findById(id)
                .map(vaccination -> {
                    vaccination.setName(vaccinationDetails.getName());
                    vaccination.setDescription(vaccinationDetails.getDescription());
                    vaccination.setDateAdministered(vaccinationDetails.getDateAdministered());
                    vaccination.setNextDueDate(vaccinationDetails.getNextDueDate());
                    vaccination.setVeterinarian(vaccinationDetails.getVeterinarian());
                    vaccination.setNotes(vaccinationDetails.getNotes());
                    return ResponseEntity.ok(vaccinationRepository.save(vaccination));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable Long petId, @PathVariable Long id) {
        return vaccinationRepository.findById(id)
                .map(vaccination -> {
                    vaccinationRepository.delete(vaccination);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}