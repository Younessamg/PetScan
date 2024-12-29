package ma.ensa.pet.repository;

import ma.ensa.pet.model.ScanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {
    List<ScanHistory> findByQrCodeIdOrderByScanDateTimeDesc(Long qrCodeId);
    List<ScanHistory> findByQrCodeIdAndScanDateTimeBetween(Long qrCodeId, LocalDateTime start, LocalDateTime end);
}