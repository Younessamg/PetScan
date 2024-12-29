
package ma.ensa.pet.repository;

import ma.ensa.pet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerId(Long ownerId);

    @Query("SELECT p FROM Pet p JOIN FETCH p.owner WHERE p.id = :id")
    Optional<Pet> findByIdWithOwner(Long id);
}