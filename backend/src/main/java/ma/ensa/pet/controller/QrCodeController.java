package ma.ensa.pet.controller;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.dto.PetScanResponse;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.model.QrCode;
import ma.ensa.pet.repository.QrCodeRepository;
import ma.ensa.pet.service.QrCodeService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/qrcodes")
@RequiredArgsConstructor
public class QrCodeController {
    private final QrCodeService qrCodeService;
    private final QrCodeRepository qrCodeRepository;


    @PostMapping("/generate/pet/{petId}")
    @PreAuthorize("@petSecurity.isOwner(#petId) or hasRole('ADMIN')")
    public ResponseEntity<QrCode> generateQrCode(@PathVariable Long petId) {
        return ResponseEntity.ok(qrCodeService.generateQrCode(petId));
    }

    @GetMapping("/{code}")
    public ResponseEntity<QrCode> getQrCodeByCode(@PathVariable String code) {
        return ResponseEntity.ok(qrCodeService.getQrCodeByCode(code));
    }

    @GetMapping("/image/{code}")
    public ResponseEntity<Resource> getQrCodeImage(@PathVariable String code) {
        try {
            QrCode qrCode = qrCodeService.getQrCodeByCode(code);
            Path path = Paths.get(qrCode.getImagePath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@qrCodeSecurity.isOwner(#id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteQrCode(@PathVariable Long id) {
        qrCodeService.deleteQrCode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QrCode>> getAllQrCodes() {
        return ResponseEntity.ok(qrCodeService.getAllQrCodes());
    }

    @GetMapping("/scan/{code}")
    public ResponseEntity<?> scanQrCode(@PathVariable String code) {
        try {
            QrCode qrCode = qrCodeRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("QR Code not found"));

            Pet pet = qrCode.getPet();
            // Create a response with pet details
            return ResponseEntity.ok(new PetScanResponse(
                    pet.getName(),
                    pet.getSpecies(),
                    pet.getBreed(),
                    pet.getColor(),
                    pet.getDescription(),
                    pet.getOwner().getPhoneNumber(),
                    pet.getOwner().getAddress()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error scanning QR code: " + e.getMessage());
        }
    }
}


