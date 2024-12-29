package ma.ensa.pet.service;

import ma.ensa.pet.model.Pet;
import java.util.List;

public interface PetService {
    Pet createPet(Pet pet, Long ownerId);
    Pet updatePet(Long id, Pet pet);
    void deletePet(Long id);
    Pet getPetById(Long id);
    List<Pet> getPetsByOwnerId(Long ownerId);
    List<Pet> getAllPets();
}