package ma.ensa.pet.repository;

import ma.ensa.pet.model.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    Optional<Veterinarian> findByEmail(String email);

    List<Veterinarian> findByCity(String city);

    List<Veterinarian> findByEmergencyServiceTrue();

    @Query(value = "SELECT v.* FROM veterinarians v " +
            "WHERE (6371 * acos(cos(radians(:latitude)) * cos(radians(v.latitude)) * " +
            "cos(radians(v.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(v.latitude)))) <= :distance " +
            "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(v.latitude)) * " +
            "cos(radians(v.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(v.latitude))))",
            nativeQuery = true)
    List<Veterinarian> findNearbyVeterinarians(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distanceInKm
    );

    @Query("SELECT v FROM Veterinarian v WHERE " +
            "v.emergencyService = true AND " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(v.latitude)) * " +
            "cos(radians(v.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(v.latitude)))) <= :distance")
    List<Veterinarian> findNearbyEmergencyVeterinarians(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distanceInKm
    );
}