package ma.ensa.pet.service;

import ma.ensa.pet.model.ScanHistory;
import java.time.LocalDateTime;
import java.util.List;

public interface ScanHistoryService {
    ScanHistory recordScan(String qrCode, String location, String scannerIp, String additionalInfo);
    List<ScanHistory> getScanHistoryByQrCodeId(Long qrCodeId);
    List<ScanHistory> getScanHistoryByDateRange(Long qrCodeId, LocalDateTime start, LocalDateTime end);
}