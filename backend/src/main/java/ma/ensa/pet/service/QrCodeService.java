package ma.ensa.pet.service;

import ma.ensa.pet.model.QrCode;
import java.util.List;

public interface QrCodeService {
    QrCode generateQrCode(Long petId);
    QrCode getQrCodeByCode(String code);
    void deleteQrCode(Long id);
    List<QrCode> getAllQrCodes();
    String generateUniqueCode();
    QrCode getQrCodeById(Long id);
}