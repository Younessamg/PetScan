package ma.ensa.pet.service.impl;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.QrCode;
import ma.ensa.pet.model.ScanHistory;
import ma.ensa.pet.repository.ScanHistoryRepository;
import ma.ensa.pet.service.QrCodeService;
import ma.ensa.pet.service.ScanHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScanHistoryServiceImpl implements ScanHistoryService {
    private final ScanHistoryRepository scanHistoryRepository;
    private final QrCodeService qrCodeService;

    @Override
    public ScanHistory recordScan(String qrCode, String location, String scannerIp, String additionalInfo) {
        QrCode qrCodeEntity = qrCodeService.getQrCodeByCode(qrCode);

        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setQrCode(qrCodeEntity);
        scanHistory.setLocation(location);
        scanHistory.setScannerIp(scannerIp);
        scanHistory.setAdditionalInfo(additionalInfo);

        return scanHistoryRepository.save(scanHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScanHistory> getScanHistoryByQrCodeId(Long qrCodeId) {
        return scanHistoryRepository.findByQrCodeIdOrderByScanDateTimeDesc(qrCodeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScanHistory> getScanHistoryByDateRange(Long qrCodeId, LocalDateTime start, LocalDateTime end) {
        return scanHistoryRepository.findByQrCodeIdAndScanDateTimeBetween(qrCodeId, start, end);
    }
}