package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.ScanHistory;
import ma.ensa.pet.service.ScanHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/scans")
@RequiredArgsConstructor
public class ScanHistoryController {
    private final ScanHistoryService scanHistoryService;

    @PostMapping("/record/{qrCode}")
    public ResponseEntity<ScanHistory> recordScan(
            @PathVariable String qrCode,
            @RequestParam(required = false) String location,
            HttpServletRequest request) {

        String scannerIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String additionalInfo = "User-Agent: " + userAgent;

        ScanHistory scanHistory = scanHistoryService.recordScan(qrCode, location, scannerIp, additionalInfo);
        return ResponseEntity.ok(scanHistory);
    }

    @GetMapping("/qrcode/{qrCodeId}")
    @PreAuthorize("@qrCodeSecurity.isOwner(#qrCodeId) or hasRole('ADMIN')")
    public ResponseEntity<List<ScanHistory>> getScanHistoryByQrCodeId(@PathVariable Long qrCodeId) {
        return ResponseEntity.ok(scanHistoryService.getScanHistoryByQrCodeId(qrCodeId));
    }

    @GetMapping("/qrcode/{qrCodeId}/daterange")
    @PreAuthorize("@qrCodeSecurity.isOwner(#qrCodeId) or hasRole('ADMIN')")
    public ResponseEntity<List<ScanHistory>> getScanHistoryByDateRange(
            @PathVariable Long qrCodeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(scanHistoryService.getScanHistoryByDateRange(qrCodeId, start, end));
    }
}