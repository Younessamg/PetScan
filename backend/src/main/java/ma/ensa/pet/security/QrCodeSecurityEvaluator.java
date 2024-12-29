package ma.ensa.pet.security;

import lombok.RequiredArgsConstructor;
import ma.ensa.pet.model.QrCode;
import ma.ensa.pet.model.User;
import ma.ensa.pet.service.QrCodeService;
import ma.ensa.pet.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("qrCodeSecurity")
@RequiredArgsConstructor
public class QrCodeSecurityEvaluator {
    private final QrCodeService qrCodeService;
    private final UserService userService;

    public boolean isOwner(Long qrCodeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String currentUserEmail = authentication.getName();
        User user = userService.getUserByEmail(currentUserEmail);
        QrCode qrCode = qrCodeService.getQrCodeById(qrCodeId);

        return qrCode.getPet().getOwner().getId().equals(user.getId());
    }
}