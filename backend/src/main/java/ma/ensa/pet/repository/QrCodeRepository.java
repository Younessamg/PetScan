package ma.ensa.pet.repository;

import ma.ensa.pet.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByCode(String code);
    boolean existsByCode(String code);
}