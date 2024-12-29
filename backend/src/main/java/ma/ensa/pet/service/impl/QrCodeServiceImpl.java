package ma.ensa.pet.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.Pet;
import ma.ensa.pet.model.QrCode;
import ma.ensa.pet.repository.QrCodeRepository;
import ma.ensa.pet.service.PetService;
import ma.ensa.pet.service.QrCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeRepository qrCodeRepository;
    private final PetService petService;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.qrcode.storage.location}")
    private String qrCodeStorageLocation;

    @Value("${app.qrcode.base-url}")
    private String baseUrl;

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }
        return code.toString();
    }

    @Override
    public String generateUniqueCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (qrCodeRepository.existsByCode(code));
        return code;
    }

    @Override
    public QrCode generateQrCode(Long petId) {
        try {
            Pet pet = petService.getPetById(petId);
            String uniqueCode = generateUniqueCode();

            // Create a URL that will be encoded in the QR code
            // This URL should point to your frontend page that will show pet info
            String qrCodeContent = baseUrl + "/scan/" + uniqueCode;

            // Generate and save QR code image
            String imagePath = generateQRCodeImage(qrCodeContent, uniqueCode);

            QrCode qrCode = new QrCode();
            qrCode.setCode(uniqueCode);
            qrCode.setImagePath(imagePath);
            qrCode.setPet(pet);

            return qrCodeRepository.save(qrCode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public QrCode getQrCodeById(Long id) {
        return qrCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QR Code not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public QrCode getQrCodeByCode(String code) {
        return qrCodeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("QR code not found"));
    }

    @Override
    public void deleteQrCode(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QR code not found"));

        try {
            Files.deleteIfExists(Paths.get(qrCode.getImagePath()));
        } catch (IOException e) {
            // Log the error but continue with database deletion
        }

        qrCodeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QrCode> getAllQrCodes() {
        return qrCodeRepository.findAll();
    }


    private String generateQRCodeImage(String content, String fileName) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        var bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 350, 350);

        Path directoryPath = Paths.get(qrCodeStorageLocation);
        Files.createDirectories(directoryPath);

        Path filePath = directoryPath.resolve(fileName + ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        return filePath.toString();
    }
}