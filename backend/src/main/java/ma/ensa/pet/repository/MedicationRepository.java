package ma.ensa.pet.repository;

import ma.ensa.pet.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByPetId(Long petId);
}