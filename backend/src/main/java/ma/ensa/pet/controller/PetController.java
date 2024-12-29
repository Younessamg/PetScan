package ma.ensa.pet.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("/owner/{ownerId}")
    @PreAuthorize("@userSecurity.isCurrentUser(#ownerId) or hasRole('ADMIN')")
    public ResponseEntity<Pet> createPet(
            @PathVariable Long ownerId,
            @Valid @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.createPet(pet, ownerId));

    }

    @PutMapping("/{id}")
    @PreAuthorize("@petSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<Pet> updatePet(
            @PathVariable Long id,
            @Valid @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.updatePet(id, pet));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@petSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("@userSecurity.isCurrentUser(#ownerId) or hasRole('ADMIN')")
    public ResponseEntity<List<Pet>> getPetsByOwnerId(@PathVariable Long ownerId) {
        return ResponseEntity.ok(petService.getPetsByOwnerId(ownerId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }
}