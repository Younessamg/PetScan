package ma.ensa.pet.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.model.User;
import ma.ensa.pet.repository.PetRepository;
import ma.ensa.pet.service.PetService;
import ma.ensa.pet.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserService userService;

    @Override
    public Pet createPet(Pet pet, Long ownerId) {
        User owner = userService.getUserById(ownerId);
        pet.setOwner(owner);
        return petRepository.save(pet);
    }

    @Override
    public Pet updatePet(Long id, Pet pet) {
        Pet existingPet = getPetById(id);
        existingPet.setName(pet.getName());
        existingPet.setSpecies(pet.getSpecies());
        existingPet.setBreed(pet.getBreed());
        existingPet.setColor(pet.getColor());
        existingPet.setDescription(pet.getDescription());
        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getPetsByOwnerId(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
}
